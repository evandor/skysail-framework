package de.twenty11.skysail.server.core.restlet.filter;

import org.apache.shiro.SecurityUtils;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CookieSetting;

import de.twenty11.skysail.api.favorites.Favorite;
import de.twenty11.skysail.api.favorites.FavoritesService;
import de.twenty11.skysail.server.Constants;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.core.restlet.utils.CookiesUtils;

public class CheckFavoritesFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected FilterResult beforeHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        super.beforeHandle(resource, response, responseWrapper);
        Request request = resource.getRequest();
        String favoriteFlag = request.getResourceRef().getQueryAsForm().getFirstValue(Constants.QUERY_PARAM_FAVORITE);
        if (favoriteFlag == null || !(resource instanceof SkysailServerResource)) {
            return FilterResult.CONTINUE;
        }
        String name = ((SkysailServerResource<?>) resource).getLinkName();
        String img = ((SkysailServerResource<?>) resource).getImageRef();
        String favoritesFromCookie = CookiesUtils.getFavoritesFromCookie(resource.getRequest());
        CookieSetting cookieSetting;
        String value = "";

        Application app = resource.getApplication();
        SkysailApplication skysailApp = (SkysailApplication) app;
        FavoritesService service = skysailApp.getFavoritesService();

        String username = (String) SecurityUtils.getSubject().getPrincipal();
        String link = request.getResourceRef().toString(false, false);
        Favorite favorite = new Favorite(username, name, link);
        if ("true".equalsIgnoreCase(favoriteFlag.trim())) {
            //value = CookiesUtils.createFavoriteEntry(request, name, img);
            favorite.setFavoriteImg(img);
            service.add(favorite);
            if (favoritesFromCookie != null) {
                value = favoritesFromCookie.trim().length() == 0 ? value : favoritesFromCookie + value + "|";
            }
        } else {
            service.remove(favorite);
//            if (favoritesFromCookie != null) {
//                value = favoritesFromCookie.replace(CookiesUtils.createFavoriteEntry(request, name, img), "");
//            }
        }
//        cookieSetting = new CookieSetting(Constants.COOKIE_NAME_FAVORITES, value);
//        cookieSetting.setPath("/");
//        response.getCookieSettings().add(cookieSetting);
//
//        resource.getContext().getAttributes().put("currentFavoritesCookieValue", value);
        return FilterResult.CONTINUE;
    }

    private void getFavoritesService() {
        // TODO Auto-generated method stub

    }

}
