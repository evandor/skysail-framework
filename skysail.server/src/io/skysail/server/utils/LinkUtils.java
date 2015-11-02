package io.skysail.server.utils;

import io.skysail.api.links.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
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

    public static List<Link> fromResources(SkysailServerResource<?> skysailServerResource, Object entity,
            Class<? extends SkysailServerResource<?>>[] classes) {
        List<Link> links = Arrays.stream(classes).map(determineLink(skysailServerResource))//
                .filter(lh -> {
                    return lh != null;// && lh.isApplicable();
                }).collect(Collectors.toList());

        links.addAll(getAssociatedLinks(entity, skysailServerResource));

        return links;
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

    private static Link fromResource(SkysailServerResource<?> skysailServerResource,
            Class<? extends SkysailServerResource<?>> ssr) {
        if (noRouteBuilderFound(skysailServerResource.getApplication(), ssr)) {
            log.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
            return null;
        }
        return createLink(skysailServerResource, ssr);
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
                .image(MediaType.TEXT_HTML,
                        resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_GLYPH) : null)
                .build();

        log.debug("created link {}", link);
        return link;
    }

    private static Link createLink(SkysailServerResource<?> skysailServerResource,
            Class<? extends SkysailServerResource<?>> resourceClass) {
        return createLink2(skysailServerResource, resourceClass, null);
    }

    private static Link createLink2(SkysailServerResource<?> skysailServerResource,
            Class<? extends SkysailServerResource<?>> resourceClass, Object object) {
        SkysailApplication app = skysailServerResource.getApplication();
        RouteBuilder routeBuilder = app.getRouteBuilders(resourceClass).get(0);
        Optional<SkysailServerResource<?>> resource = createNewInstance(resourceClass);

        LinkRelation relation = resource.isPresent() ? resource.get().getLinkRelation() : LinkRelation.ALTERNATE;
        String uri = determineUri2(skysailServerResource, resourceClass, routeBuilder);
        Reference resourceRef = skysailServerResource.getRequest().getResourceRef();
        if (uri.equals(resourceRef.getPath())) {
            relation = LinkRelation.SELF;
        }
        Link link = new Link.Builder(uri)
                .definingClass(resourceClass)
                .relation(relation)
                .title(resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_TITLE) : "unknown")
                .authenticationNeeded(routeBuilder.needsAuthentication())
                .needsRoles(routeBuilder.getRolesForAuthorization())
                .image(MediaType.TEXT_HTML,
                        resource.isPresent() ? resource.get().getFromContext(ResourceContextId.LINK_GLYPH) : null)
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

    private static String determineUri2(SkysailServerResource<?> skysailServerResource,
            Class<? extends SkysailServerResource<?>> resourceClass, RouteBuilder routeBuilder) {
        SkysailApplication app = skysailServerResource.getApplication();
        String result = "/" + app.getName() + routeBuilder.getPathTemplate(app.getApiVersion());
        try {
            Set<String> types = resourceClass.newInstance().getRestrictedToMediaTypes();
            if (types.size() == 1) {
                result += "?media=" + types.iterator().next();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.warn(e.getMessage());
        }
        return result;
    }

    private static boolean noRouteBuilderFound(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr) {
        return app.getRouteBuilders(ssr).size() == 0;
    }

    private static Function<? super Class<? extends SkysailServerResource<?>>, ? extends Link> determineLink(
            SkysailServerResource<?> skysailServerResource) {
        return cls -> LinkUtils.fromResource(skysailServerResource, cls);
    }

    /**
     * if the current resource is a {@link ListServerResource}, the associated
     * EntityServerResource (if existent) is analyzed for its own links.
     *
     * <p>
     * For each entity of the listServerResource, and for each associated link
     * (which serves as a template), a new link is created and is having its
     * path placeholders substituted. So, if the current ListServerResource has
     * a list with two entities of a type which defines three classes in its
     * getLinks method, we'll get six links in the result.
     * </p>
     */
    private static <T> List<? extends Link> getAssociatedLinks(Object entity,
            SkysailServerResource<T> skysailServerResource) {
        if (!(skysailServerResource instanceof ListServerResource)) {
            return Collections.emptyList();
        }
        ListServerResource<?> listServerResource = (ListServerResource<?>) skysailServerResource;
        List<Class<? extends SkysailServerResource<?>>> entityResourceClasses = listServerResource
                .getAssociatedServerResources();
        List<Link> result = new ArrayList<>();

        if (entityResourceClasses != null && entity instanceof List) {
            List<SkysailServerResource<?>> esrs = ResourceUtils.createSkysailServerResources(entityResourceClasses,
                    skysailServerResource);

            for (SkysailServerResource<?> esr : esrs) {
                List<Link> entityLinkTemplates = esr.getAuthorizedLinks();
                for (Object object : (List<?>) entity) {
                    //String id = guessId(object);
                    //Map<String, String> substitutions = PathUtils.getSubstitutions(object, skysailServerResource);
                    entityLinkTemplates.stream().filter(lh -> {
                        return lh.getRole().equals(LinkRole.DEFAULT);
                    }).forEach(link -> addLink(link, esr, object, listServerResource, result));
                }
            }
        }
        return result;

    }

//    private static String guessId(Object object) {
//        if (object instanceof Identifiable) {
//            Identifiable identifiable = (Identifiable) object;
//            if (identifiable.getId() != null) {
//                return identifiable.getId().replace("#", "");
//            }
//        }
//        if (object instanceof Map) {
//            Map<String, Object> map = (Map) object;
//            if (map.get("@rid") != null) {
//                return map.get("@rid").toString().replace("#", "");
//            }
//            if (map.get("id") != null) {
//                return map.get("id").toString().replace("#", "");
//            }
//        }
//        log.warn("not able to determine id");
//        return "NO_ID";
//    }

    private static void addLink(Link linkTemplate, Resource entityResource, Object object, ListServerResource<?> resource,
            List<Link> result) {
        String path = linkTemplate.getUri();
        Map<String, String> substitutions = PathUtils.getSubstitutions(object, resource);
        String href = path;

        for (Entry<String, String> entry : substitutions.entrySet()) {
            String substitutable = new StringBuilder("{").append(entry.getKey()).append("}").toString();
            if (path.contains(substitutable)) {
                href = href.replace(substitutable, entry.getValue());
            }
        }
//
//        if (substitutions.get("id") != null && path.contains("{") && path.contains("}")) {
//            href = href.replaceFirst(StringParserUtils.placeholderPattern.toString(), substitutions.get("id"));
//        }

        String refId = substitutions.values().stream().collect(Collectors.joining("|"));
        Link newLink = new Link.Builder(linkTemplate)
                .uri(href).role(LinkRole.LIST_VIEW).relation(LinkRelation.ITEM).refId(refId).build();
        result.add(newLink);
    }
}
