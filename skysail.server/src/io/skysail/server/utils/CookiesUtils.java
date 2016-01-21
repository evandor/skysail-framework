package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.*;

import de.twenty11.skysail.server.Constants;

public class CookiesUtils {
    
    public static CookieSetting createCookie(String name, String path) {
        CookieSetting cookieSetting = new CookieSetting(name, null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath(path);
        //cookieSetting.setComment("cookie to remember where to redirect to after posts or puts");
        cookieSetting.setMaxAge(300);
        return cookieSetting;
    }


    /**
     * cookie is set by navbar.stg 
     */
    public static String getTemplateFromCookie(Request request) {
        return returnCookieOrNull(request,Constants.COOKIE_NAME_TEMPLATE);
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
