package io.skysail.server.restlet.filter;

import io.skysail.api.favorites.Favorite;
import io.skysail.api.favorites.FavoritesService;
import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRole;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.CookiesUtils;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.Request;

import de.twenty11.skysail.server.Constants;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class CheckFavoritesFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected FilterResult beforeHandle(R resource, ResponseWrapper<T> responseWrapper) {
        super.beforeHandle(resource, responseWrapper);

        Application app = resource.getApplication();
        SkysailApplication skysailApp = (SkysailApplication) app;
        FavoritesService service = skysailApp.getFavoritesService();
        if (service == null) {
            return FilterResult.CONTINUE;
        }

        String username = (String) SecurityUtils.getSubject().getPrincipal();

        Request request = resource.getRequest();
        if (request.getResourceRef() == null || request.getResourceRef().getQueryAsForm() == null) {
            addFavoritesAsLinks(resource, service.get(username));
            return FilterResult.CONTINUE;
        }
        String favoriteFlag = request.getResourceRef().getQueryAsForm().getFirstValue(Constants.QUERY_PARAM_FAVORITE);
        if (favoriteFlag == null || !(resource instanceof SkysailServerResource)) {
            addFavoritesAsLinks(resource, service.get(username));
            return FilterResult.CONTINUE;
        }

        String name = "name";
        String img = "img";
        String favoritesFromCookie = CookiesUtils.getFavoritesFromCookie(resource.getRequest());
        String value = "";

        String link = request.getResourceRef().toString(false, false);
        Favorite favorite = new Favorite().setUsername(username).setFavoriteName(name).setFavoriteLink(link);
        if ("true".equalsIgnoreCase(favoriteFlag.trim())) {
            // value = CookiesUtils.createFavoriteEntry(request, name, img);
            favorite.setFavoriteImg(img);
            service.add(favorite);
            if (favoritesFromCookie != null) {
                value = favoritesFromCookie.trim().length() == 0 ? value : favoritesFromCookie + value + "|";
            }
        } else {
            service.remove(favorite);
            // if (favoritesFromCookie != null) {
            // value =
            // favoritesFromCookie.replace(CookiesUtils.createFavoriteEntry(request,
            // name, img), "");
            // }
        }
        // cookieSetting = new CookieSetting(Constants.COOKIE_NAME_FAVORITES,
        // value);
        // cookieSetting.setPath("/");
        // response.getCookieSettings().add(cookieSetting);
        //
        // resource.getContext().getAttributes().put("currentFavoritesCookieValue",
        // value);

        addFavoritesAsLinks(resource, service.get(username));

        return FilterResult.CONTINUE;
    }

    private void addFavoritesAsLinks(R resource, List<Favorite> favorites) {
        favorites.stream().forEach(
                fav -> {
                    resource.getLinks().add(
                            new Link.Builder(fav.getFavoriteLink()).title(fav.getFavoriteName())
                                    .role(LinkRole.APPLICATION_NAVIGATION).build());
                });
    }

}
