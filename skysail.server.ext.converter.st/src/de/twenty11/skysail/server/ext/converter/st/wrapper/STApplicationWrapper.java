package de.twenty11.skysail.server.ext.converter.st.wrapper;

import java.util.stream.Collectors;

import org.restlet.Restlet;
import org.restlet.routing.Filter;
import org.restlet.routing.Route;
import org.restlet.routing.TemplateRoute;
import org.restlet.util.RouteList;

import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.security.AuthenticatedAuthorizer;

public class STApplicationWrapper {

    private SkysailApplication application;

    public STApplicationWrapper(SkysailServerResource<?> resource) {
        this.application = resource.getApplication();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(application.getClass().getSimpleName()).append("<br>\n");
        RouteList routeList = application.getRoutes();
        String routes = routeList.stream().map(route -> renderRoute(route)).collect(Collectors.joining());
        sb.append("Router: <ul>").append(routes).append("</ul>\n");
        return sb.toString();
    }

    private String renderRoute(Route route) {
        StringBuilder sb = new StringBuilder("<li>");
        if (route instanceof TemplateRoute) {
            TemplateRoute templateRoute = (TemplateRoute)route;
            String pattern = templateRoute.getTemplate().getPattern();
            sb.append("<a href='").append("/clipboard").append(pattern).append("'>").append(pattern).append("</a>");
            sb.append(" -> ");
            Restlet nextRoute = templateRoute.getNext();
            sb.append(nextRoute.getClass().getSimpleName());
            if (nextRoute instanceof AuthenticatedAuthorizer) {
                sb.append(" (-> ").append(((Filter) nextRoute).getNext().toString()).append(")");
            }
        } else {
            sb.append(route.toString());
        }
        sb.append("</li>");
        return sb.toString();
    }

}
