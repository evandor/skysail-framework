package io.skysail.server.converter.wrapper;

import io.skysail.api.favorites.Favorite;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.server.converter.Breadcrumb;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.routing.Route;
import org.restlet.util.RouteList;

import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Slf4j
public class Breadcrumbs {

    private FavoritesService favoritesService;
    
    public Breadcrumbs(FavoritesService favoritesService) {
        if (favoritesService == null) {
            log.info("favoritesService was null when creating breadcrumbs");
        }
        this.favoritesService = favoritesService;
    }

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
            match = best;
            String value = segments.get(i);
            Object substitutions = resource.getContext().getAttributes().get(ResourceContextId.PATH_SUBSTITUTION.name());
            if (substitutions instanceof Map) {
                Map<String,?> substitutionsMap = (Map<String,?>)substitutions;
                Optional<String> replacementKey = substitutionsMap.keySet().stream().filter(key -> {
                    return path.contains(key);
                }).findFirst();
                if (replacementKey.isPresent()) {
                    value = substitutionsMap.get(replacementKey.get()).toString();
                    ((Map) substitutions).remove(replacementKey.get());
                }
            }
            if (i < segments.size() - 1) {
                breadcrumbs.add(Breadcrumb.builder().href("/" + resource.getApplication().getName() + path)
                        .value(limit(value, 22)).build());
            } else {
                Boolean favoriteIndicator = getFavoriteIndicator(resource);
                breadcrumbs.add(Breadcrumb.builder().value(value).favorite(favoriteIndicator).build());
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
