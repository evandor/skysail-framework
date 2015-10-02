package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.Cookie;

import de.twenty11.skysail.server.Constants;

public class CookiesUtils {

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
