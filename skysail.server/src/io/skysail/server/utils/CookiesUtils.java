package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.Cookie;

import de.twenty11.skysail.server.Constants;

public class CookiesUtils {

    public static final String FAVORITE_DEFINITION_DELIMITER = "^";
    public static final String FAVORITES_DELIMITER = "|";

    public static String getTemplateFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_TEMPLATE);
    }
    
    public static String getMainPageFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_MAINPAGE);
    }

    public static String getFavoritesFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_FAVORITES);
    }
    
    public static String getInstallationFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_INSTALLATIONS);
    }

    public static String getEntriesPerPageFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_ENTRIES_PER_PAGE);
    }

    private static String returnCookieOrNull(Request request, String name) {
        if (request == null || request.getCookies() == null) {
            return null;
        }
        Cookie templateCookie = request.getCookies().getFirst(name);
        if (templateCookie == null) {
            return null;
        }
        return templateCookie.getValue();
    }

    public static String createFavoriteEntry(Request request, String name, String img) {
        return name+"="+img + FAVORITE_DEFINITION_DELIMITER + request.getResourceRef().toString(false, false) + FAVORITES_DELIMITER;
    }

}
