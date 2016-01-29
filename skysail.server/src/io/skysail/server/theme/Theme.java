package io.skysail.server.theme;

import org.restlet.data.CookieSetting;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.Constants;
import io.skysail.server.utils.CookiesUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Theme {

    public enum GuiFramework {
        TEXT
    }

    public enum Variant {
        HTML, SPA, JQUERYMOBILE, UIKIT, PURECSS, W2UI
    }

    public enum Option {
        DEFAULT, EDIT, DEBUG
    }

    @Getter
    private GuiFramework guiFramework = GuiFramework.TEXT;

    @Getter
    private Variant variant = Variant.HTML;

    @Getter
    private Option option = Option.DEFAULT;

    public static Theme determineFrom(Resource resource) {
        
        String themeFromRequest = resource.getQuery() != null ? resource.getQuery().getFirstValue("_theme") : null;
        if (themeFromRequest != null) {
            Theme theme = themeFromSplit(themeFromRequest, themeFromRequest.split("/"));
            CookieSetting templateCookie = CookiesUtils.createCookie(Constants.COOKIE_NAME_TEMPLATE, resource.getRequest().getResourceRef().getPath(), -1);
            templateCookie.setValue(themeFromRequest);
            resource.getResponse().getCookieSettings().add(templateCookie);
            return theme;
        }
        String themeFromCookie = CookiesUtils.getTemplateFromCookie(resource.getRequest());
        if (themeFromCookie == null) {
            themeFromCookie = "text/html";
        }
        return themeFromSplit(themeFromCookie, themeFromCookie.split("/"));
    }

    private static Theme themeFromSplit(String themeAsString, String[] split) {
        if (split.length == 2) {
            return createTheme(split[0], split[1]);
        } else if (split.length == 3) {
            return createTheme(split[0], split[1], split[2]);
        } else {
            log.warn("could not determine theme from string '{}', using Fallback '{}'", themeAsString, new Theme());
            return new Theme();
        }
    }

    public static Theme createTheme(String guiFramework, String variant) {
        return createTheme(guiFramework, variant, null);
    }

    public static Theme createTheme(String guiFramework, String variant, String option) {
        Theme theme = new Theme();
        theme.guiFramework = GuiFramework.valueOf(guiFramework.toUpperCase());
        theme.variant = Variant.valueOf(variant.toUpperCase());
        if (option != null) {
            theme.option = Option.valueOf(option.toUpperCase());
        }
        return theme;
    }

    @Override
    public String toString() {
        return new StringBuilder(guiFramework.toString()).append("/").append(variant.toString()).toString()
                .toLowerCase();
    }

}
