package de.twenty11.skysail.server.core.restlet.converter.renderer;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.resource.Resource;

import de.twenty11.skysail.api.favorites.FavoritesService;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class FavoritesRenderer {

    private static class Favorite {

        private String img;
        private String link;

        public Favorite(String img, String link) {
            this.img = img;
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public String getFavorite() {
            return new StringBuilder(img).append("<small>").append(link).append("</small>").toString();
        }
    }

    public static CharSequence favorites(String favorites, Resource resource) {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return "";
        }
        Application application = resource.getApplication();
        if (!(application instanceof SkysailApplication)) {
            return "";
        }
        FavoritesService favoritesService = ((SkysailApplication) application).getFavoritesService();
        if (favoritesService == null) {
            return "";
        }
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        List<de.twenty11.skysail.api.favorites.Favorite> favoritesList = favoritesService.get(username);
        String links = favoritesList.stream().map(f -> createFavoriteLink(f))
                .filter(f -> f != null && f.getLink() != null && f.getLink().trim().length() > 0)
                .sorted((Favorite f1, Favorite f2) -> f1.getLink().compareTo(f2.getLink())).map(f -> f.getFavorite())
                .collect(Collectors.joining("<br>\n"));

        if (links.length() > 0) {
            links = "<h6>Favorites</h6>" + links;
        }
        return links;
    }

    /**
     * Creates an html string containing a link to either create a new or remove
     * an existing favorite.
     * 
     * @param favorites
     * @param resource
     * @return
     */
    public static String favorite(String favorites, Resource resource) {
        if (!(resource instanceof SkysailServerResource)) {
            return "";
        }
        // figure out if GET request
        if (!Method.GET.equals(resource.getRequest().getMethod())) {
            return "";
        }

        SkysailApplication app = (SkysailApplication) resource.getApplication();
        FavoritesService favoritesService = app.getFavoritesService();
        if (favoritesService == null) {
            return "";
        }
        List<de.twenty11.skysail.api.favorites.Favorite> favoritesList = favoritesService.get((String) SecurityUtils
                .getSubject().getPrincipal());
        if (favoriteExists(favoritesList, resource.getRequest().getResourceRef().toString(false, false))) {
            return createRemoveFavoriteLink(resource.getRequest());
        }
        return createMakeFavoriteLink(resource.getRequest());
    }

    private static boolean favoriteExists(List<de.twenty11.skysail.api.favorites.Favorite> favoritesList, String string) {
        return favoritesList.stream().map(f -> f.getFavoriteLink()).filter(link -> link.equals(string)).findFirst()
                .isPresent();
    }

    private static Favorite createFavoriteLink(de.twenty11.skysail.api.favorites.Favorite f) {
        String img = f.getFavoriteImg();
        String href = f.getFavoriteLink();
        String favoriteName = f.getFavoriteName();
        return new Favorite(img, "&nbsp;<a href='" + href + "'>" + favoriteName + "</a>");
    }

    private static String createMakeFavoriteLink(Request request) {
        String location = request.getResourceRef().toString();
        if (!location.contains("favorite=true")) {
            if (location.contains("favorite=false")) {
                location = location.replace("favorite=false", "favorite=true");
            } else {
                location = location.contains("?") ? location + "&favorite=true" : location + "?favorite=true";
            }
        }
        return "<a href='javascript:window.location=\"" + location + "\"'><i class=\"icon-star-empty\"></i></a>";
    }

    private static String createRemoveFavoriteLink(Request request) {
        String currentPage = request.getResourceRef().toString();
        String location = currentPage;
        if (!location.contains("favorite=true")) {
            location = location.contains("?") ? location + "&favorite=false" : location + "?favorite=false";
        } else {
            location = location.replace("favorite=true", "favorite=false");
        }
        return "<a href='javascript:window.location=\"" + location + "\"'><i class=\"icon-star\"></i></a>";
    }
}
