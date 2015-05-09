package io.skysail.server.utils;

import io.skysail.api.links.*;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.MediaType;

import de.twenty11.skysail.server.core.restlet.*;

@Slf4j
public class LinkUtils {

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
        
        log.info("created link {}", link);
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
        return "/" + app.getName() + routeBuilder.getPathTemplate();
    }

    private static boolean noRouteBuilderFound(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr) {
        return app.getRouteBuilders(ssr).size() == 0;
    }
}
