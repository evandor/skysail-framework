package io.skysail.server.converter.wrapper;

import io.skysail.api.favorites.Favorite;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.server.converter.Breadcrumb;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.routing.Route;
import org.restlet.util.RouteList;

import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@RequiredArgsConstructor
public class Breadcrumbs {

    @NonNull
    private FavoritesService favoritesService;

    public List<Breadcrumb> create(@NotNull SkysailServerResource<?> resource) {
        List<Breadcrumb> result = new ArrayList<Breadcrumb>();
        result.add(homeBreadcrumb());
        List<String> segments = getCleanedSegments(resource.getReference());
        if (segments.size() > 0) {
            result.add(applicationBreadcrumb(resource));
        }
        addSegments(resource, result, segments);
        return result;
    }

    private void addSegments(SkysailServerResource<?> resource, List<Breadcrumb> result, List<String> segments) {
        String path = "";
        Route match = null;
        for (int i = 1; i < segments.size(); i++) {
            path = path + "/" + segments.get(i);
            match = handleSegment(result, segments, path, match, i, resource);
        }
    }

    private Breadcrumb applicationBreadcrumb(SkysailServerResource<?> resource) {
        String img = resource.getApplication().getFromContext(ApplicationContextId.IMG);
        String imgHtml = img != null ? "<img src='" + img + "'>" : "";
        String text = imgHtml + " " + resource.getApplication().getName();
        return Breadcrumb.builder().href("/" + resource.getApplication().getName()).value(text)
                .favorite(getFavoriteIndicator(resource)).build();
    }

    private Breadcrumb homeBreadcrumb() {
        return Breadcrumb.builder().href("/").value("<span class='glyphicon glyphicon-home'></span>").build();
    }

    private Route handleSegment(List<Breadcrumb> breadcrumbs, List<String> segments, String path, Route match, int i,
            SkysailServerResource<?> resource) {

        Request request = resource.getRequest();
        request.setResourceRef(new Reference(path));
        RouteList routes = resource.getApplication().getRoutes();
        Route best = routes.getBest(request, resource.getResponse(), 0.5f);
        if (best != match) {

//            Router router = best.getRouter();
//            if (router instanceof SkysailRouter && best instanceof TemplateRoute) {
//                SkysailRouter skysailRouter = (SkysailRouter) router;
//                Optional<RouteBuilder> routeBuilder = skysailRouter.getRoutesMap().keySet().stream().filter(key -> {
//                    return key.equals(((TemplateRoute) best).getTemplate().getPattern());
//                }).findFirst().map(key -> skysailRouter.getRoutesMap().get(key));
//                if (routeBuilder.isPresent()) {
//                    Class<? extends ServerResource> res = routeBuilder.get().getTargetClass();
//                    if (EntityServerResource.class.isAssignableFrom(res)) {
//                        try {
//                            String resourceUri = "riap://component/Todos"
//                                    + routeBuilder.get().getPathTemplate().toString()
//                                            .replace("{lid}", segments.get(i).toString());
//                            Request r = new Request(Method.GET, resourceUri);
//                            r.setProtocol(Protocol.RIAP);
//                            Response response = resource.getContext().getClientDispatcher().handle(r);
//                            Representation repr = response.getEntity();
//
//                            System.out.println(repr.getText());
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }

            match = best;
            if (i < segments.size() - 1) {
                breadcrumbs.add(Breadcrumb.builder().href("/" + resource.getApplication().getName() + path)
                        .value(limit(segments.get(i), 12)).build());
            } else {
                Boolean favoriteIndicator = getFavoriteIndicator(resource);
                breadcrumbs.add(Breadcrumb.builder().value(segments.get(i)).favorite(favoriteIndicator).build());
            }
        }
        return match;
    }

    private List<String> getCleanedSegments(Reference reference) {
        List<String> segments = reference.getSegments();
        List<String> results = new ArrayList<String>();
        for (String segment : segments) {
            if (!StringUtils.isBlank(segment)) {
                results.add(segment);
            }
        }
        return results;
    }

    private String limit(String value, int lengthLimit) {
        if (value == null) {
            return "";
        }
        if (value.length() <= lengthLimit) {
            return value;
        }
        return value.substring(0, lengthLimit - 3).concat("...");
    }

    private synchronized Boolean getFavoriteIndicator(SkysailServerResource<?> resource) {
        if (favoritesService == null) {
            return null;
        }
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        List<Favorite> list = favoritesService.get(username);
        if (list.size() == 0) {
            return false;
        }
        String link = resource.getRequest().getResourceRef().toString(false, false);
        return list.stream().filter(l -> {
            return l.getFavoriteLink().equals(link);
        }).findFirst().isPresent();
    }

}
