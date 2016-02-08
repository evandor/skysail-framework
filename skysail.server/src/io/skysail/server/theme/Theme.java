package io.skysail.server.theme;

import org.restlet.data.CookieSetting;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.Constants;
import io.skysail.server.utils.CookiesUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Theme {

    private static final String DEFAULT_TEMPLATE = "bootstrap";

//    public enum GuiFramework {
//        TEXT, TIMELINE
//    }

    public enum Variant {
        BOOTSTRAP, SPA, JQUERYMOBILE, UIKIT, PURECSS, W2UI, TIMELINE, HOME
    }

    public enum Option {
        DEFAULT, EDIT, DEBUG
    }

//    @Getter
//    private GuiFramework guiFramework = GuiFramework.TEXT;

    @Getter
    private Variant variant = Variant.BOOTSTRAP;

    @Getter
    private Option option = Option.DEFAULT;

    public static Theme determineFrom(Resource resource, org.restlet.representation.Variant target) {
        String themeFromRequest = resource.getQuery() != null ? resource.getQuery().getFirstValue("_theme") : null;
        if (themeFromRequest != null) {
            Theme theme = themeFromSplit(themeFromRequest, themeFromRequest.split("/"));
            CookieSetting templateCookie = CookiesUtils.createCookie(Constants.COOKIE_NAME_TEMPLATE, resource.getRequest().getResourceRef().getPath(), -1);
            templateCookie.setValue(themeFromRequest);
            resource.getResponse().getCookieSettings().add(templateCookie);
            return theme;
        }
        String themeToUse = CookiesUtils.getTemplateFromCookie(resource.getRequest());
        if (themeToUse == null) {
            themeToUse = DEFAULT_TEMPLATE;
        }
        if (!target.getMediaType().toString().equals("text/html")) {
            themeToUse = target.getMediaType().toString();
        }
        return themeFromSplit(themeToUse, themeToUse.split("/"));
    }

    private static Theme themeFromSplit(String themeAsString, String[] split) {
        if (split.length == 1) {
            return createTheme(split[0]);
        } else if (split.length == 2) {
            return createTheme(split[0], split[1]);
        } else {
            log.warn("could not determine theme from string '{}', using Fallback '{}'", themeAsString, new Theme());
            return new Theme();
        }
    }

    public static Theme createTheme(String variant) {
        return createTheme(variant, null);
    }

    public static Theme createTheme(String variant, String option) {
        Theme theme = new Theme();
       //theme.guiFramework = GuiFramework.valueOf(guiFramework.toUpperCase());
        theme.variant = Variant.valueOf(variant.toUpperCase());
        if (option != null && !option.equals("*")) {
            theme.option = Option.valueOf(option.toUpperCase());
        }
        return theme;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(variant.toString()).toString()
                .toLowerCase();
    }

}
