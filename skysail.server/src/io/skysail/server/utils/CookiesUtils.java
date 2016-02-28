package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;

import io.skysail.server.Constants;
import io.skysail.server.rendering.RenderingMode;

public class CookiesUtils {
    
    public static CookieSetting createCookie(String name, String path, int maxAgeInSeconds) {
        CookieSetting cookieSetting = new CookieSetting(name, null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath(path);
        //cookieSetting.setComment("cookie to remember where to redirect to after posts or puts");
        cookieSetting.setMaxAge(maxAgeInSeconds);
        return cookieSetting;
    }


    /**
     * cookie is set by navbar.stg 
     */
    public static String getThemeFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_THEME);
    }

    public static RenderingMode getModeFromCookie(Request request) {
         String returnCookieOrNull = returnCookieOrNull(request,Constants.COOKIE_NAME_MODE);
         if (returnCookieOrNull == null) {
             return RenderingMode.DEFAULT;
         }
         return RenderingMode.valueOf(returnCookieOrNull.toUpperCase());
    }

    public static String getMainPageFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_MAINPAGE);
    }

    public static String getInstallationFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_INSTALLATIONS);
    }

    public static String getEntriesPerPageFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_ENTRIES_PER_PAGE);
    }

    public static String getReferrerFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_REFERRER);
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


    
}
