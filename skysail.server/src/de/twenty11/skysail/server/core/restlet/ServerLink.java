package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;

import java.util.function.Consumer;

import org.restlet.data.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.app.SkysailApplication;

public class ServerLink extends Link {

    private static final Logger logger = LoggerFactory.getLogger(ServerLink.class);

    public ServerLink(String substring) {
        super(new Link.Builder(substring));
    }

    public static Link fromResource(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr) {
        return fromResource(app, ssr, null);
    }

    public static Link fromResource(SkysailApplication app, Class<? extends SkysailServerResource<?>> ssr, String title) {
        if (app.getRouteBuilders(ssr).size() == 0) {
            logger.warn("problem with linkheader for resource {}; no routeBuilder was found.", ssr.getSimpleName());
            return null;
        }
        RouteBuilder routeBuilder = app.getRouteBuilders(ssr).get(0);
        String path = "/" + app.getName() + routeBuilder.getPathTemplate();
        LinkRelation defaultLinkRelation = LinkRelation.ALTERNATE;
        String glyph = "";
        Consumer<? super Link> pathSubstitutions = null;
        try {
            SkysailServerResource<?> newInstance = ssr.newInstance();
            defaultLinkRelation = newInstance.getLinkRelation();
            title = title != null ? title : newInstance.getFromContext(ResourceContextId.LINK_TITLE);
            glyph = newInstance.getFromContext(ResourceContextId.LINK_GLYPH);
            pathSubstitutions = newInstance.getPathSubstitutions();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        boolean needsAuthentication = routeBuilder.needsAuthentication();
        Predicate<String[]> rolesPredicate = routeBuilder.getRolesForAuthorization();
        Link linkheader = new Link.Builder(path).relation(defaultLinkRelation).title(title == null ? "unknown" : title)
                .authenticationNeeded(needsAuthentication).needsRoles(rolesPredicate).build();
        //System.out.println(linkheader); TODO this line gets accessed quite often, ... check!
        linkheader.setImage(MediaType.TEXT_HTML, glyph);
        if (pathSubstitutions != null) {
            //pathSubstitutions.accept(linkheader);
        }
        return linkheader;
    }
}