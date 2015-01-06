package de.twenty11.skysail.server.core.restlet;

import org.restlet.data.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.app.SkysailApplication;

public class ServerLink extends Linkheader {

    private static final Logger logger = LoggerFactory.getLogger(ServerLink.class);

    public ServerLink(String substring) {
        super(new Linkheader.Builder(substring));
    }

    public static Linkheader fromResource(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr) {
        return fromResource(app, ssr, null);
    }

    public static Linkheader fromResource(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr,
            String title) {
        if (app.getRouteBuilders(ssr).size() == 0) {
            logger.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
            return null;
        }
        RouteBuilder routeBuilder = app.getRouteBuilders(ssr).get(0);
        String path = "/" + app.getName() + routeBuilder.getPathTemplate();
        LinkHeaderRelation defaultLinkRelation = LinkHeaderRelation.ALTERNATE;
        String imageRef = "";
        try {
            SkysailServerResource<?> newInstance = ssr.newInstance();
            defaultLinkRelation = newInstance.getLinkRelation();
            title = title != null ? title : newInstance.getFromContext(ResourceContextId.LINK_TITLE);
            imageRef = "";//newInstance.getImageRef();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        boolean needsAuthentication = routeBuilder.needsAuthentication();
        Predicate<String[]> rolesPredicate = routeBuilder.getRolesForAuthorization();
        Linkheader linkheader = new Linkheader.Builder(path).relation(defaultLinkRelation).title(title == null ? "unknown" : title)
                .authenticationNeeded(needsAuthentication).needsRoles(rolesPredicate).build();
        linkheader.setImage(MediaType.TEXT_HTML, imageRef);
        return linkheader;
    }
}