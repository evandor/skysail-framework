package io.skysail.server.utils;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.*;
import io.skysail.api.utils.StringParserUtils;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.*;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.MediaType;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.core.restlet.*;

@Slf4j
public class LinkUtils {

    private static Pattern pattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);

    public static Link fromResource(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr) {
        return fromResource(app, ssr, null);
    }

    /**
     * tries to create a link (with given title) for the provided
     * SkysailServerResource and SkysailApplication.
     */
    public static Link fromResource(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr, String title) {
        if (noRouteBuilderFound(app, ssr)) {
            log.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
            return null;
        }
        return createLink(app, ssr, title);
    }

    public static String replaceValues(final String template, Map<String, Object> attributes) {

        final StringBuffer sb = new StringBuffer();
        final Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            final String key = matcher.group(1);
            final Object replacement = attributes.get(key);
            if (replacement == null) {
                log.debug("Template contains unmapped key: " + key);
            } else {
                matcher.appendReplacement(sb, replacement.toString());
            }
        }
        matcher.appendTail(sb);
        return sb.toString();

    }

    private static Link createLink(SkysailApplication app, Class<? extends SkysailServerResource<?>> resourceClass,
            String title) {

        RouteBuilder routeBuilder = app.getRouteBuilders(resourceClass).get(0);
        Optional<SkysailServerResource<?>> resource = createNewInstance(resourceClass);

        Link link = new Link.Builder(determineUri(app, routeBuilder))
            .definingClass(resourceClass)
            .relation(resource.isPresent() ? resource.get().getLinkRelation() : LinkRelation.ALTERNATE)
            .title(resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_TITLE) : "unknown")
            .authenticationNeeded(routeBuilder.needsAuthentication())
            .needsRoles(routeBuilder.getRolesForAuthorization())
            .image(MediaType.TEXT_HTML, resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_GLYPH) : null)
            .build();

        log.debug("created link {}", link);
        return link;
    }

    private static Optional<SkysailServerResource<?>> createNewInstance(
            Class<? extends SkysailServerResource<?>> resource) {
        SkysailServerResource<?> newInstance = null;
        try {
            newInstance = resource.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.ofNullable(newInstance);
    }

    private static String determineUri(SkysailApplication app, RouteBuilder routeBuilder) {
        return "/" + app.getName() + routeBuilder.getPathTemplate(app.getApiVersion());
    }

    private static boolean noRouteBuilderFound(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr) {
        return app.getRouteBuilders(ssr).size() == 0;
    }

    public static List<Link> fromResources(SkysailServerResource<?> skysailServerResource, Object entity, Class<? extends SkysailServerResource<?>>[] classes) {
         List<Link> links = Arrays.stream(classes)
                .map(cls -> LinkUtils.fromResource(skysailServerResource.getApplication(), cls))//
                .filter(lh -> {
                    return lh != null;// && lh.isApplicable();
                }).collect(Collectors.toList());

         links.addAll(getAssociatedLinks(entity, skysailServerResource));

         return links;
    }

    private  static <T> List<? extends Link> getAssociatedLinks(Object entity, SkysailServerResource<T> skysailServerResource) {
        if (!(skysailServerResource instanceof ListServerResource)) {
            return Collections.emptyList();
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) skysailServerResource;
        List<Class<? extends SkysailServerResource<?>>> entityResourceClasses = listServerResource
                .getAssociatedServerResources();
        // List<Class<? extends EntityServerResource<?>>> entityResourceClass =
        // listServerResource.getAssociatedEntityResources();
        //T entity = getCurrentEntity();
        List<Link> result = new ArrayList<>();

        if (entityResourceClasses != null && entity instanceof List) {
            List<SkysailServerResource<?>> esrs = ResourceUtils.createSkysailServerResources(entityResourceClasses,
                    skysailServerResource);

            for (SkysailServerResource<?> esr : esrs) {
                List<Link> entityLinkTemplates = esr.getAuthorizedLinks();
                for (Object object : (List<?>) entity) {
                    String id = guessId(object);
                    entityLinkTemplates.stream().filter(lh -> {
                        return lh.getRole().equals(LinkRole.DEFAULT);
                    }).forEach(link -> addLink(link, esr, id, listServerResource, result));
                }
            }
        }
        return result;

    }

    private static String guessId(Object object) {
        if (object instanceof Identifiable) {
            Identifiable identifiable = (Identifiable) object;
            return identifiable.getId().replace("#", "");
        }
        if (object instanceof Map) {
            Map<String, Object> map = (Map) object;
            if (map.get("@rid") != null) {
                return map.get("@rid").toString().replace("#", "");
            }
            if (map.get("id") != null) {
                return map.get("id").toString().replace("#", "");
            }
        }
        Map<String, Object> map = OrientDbUtils.toMap(object);
        if (map != null) {
            if (map.get("@rid") != null) {
                return map.get("@rid").toString().replace("#", "");
            }
        }

        return "NO_ID";
    }

    private static void addLink(Link linkTemplate, Resource entityResource, String id, ListServerResource<?> resource,
            List<Link> result) {
        String path = linkTemplate.getUri();

        //String href = StringParserUtils.substitutePlaceholders(path, entityResource);
        String href = path;
        // hmmm... last resort
        if (id != null && path.contains("{") && path.contains("}")) {
            href = href.replaceFirst(StringParserUtils.placeholderPattern.toString(), id);
        }

        Link newLink = new Link.Builder(linkTemplate)// .uri()
                .uri(href).role(LinkRole.LIST_VIEW).relation(LinkRelation.ITEM).refId(id).build();
        // substituePlaceholders(entityResource, id).build()
        result.add(newLink);
    }
}
