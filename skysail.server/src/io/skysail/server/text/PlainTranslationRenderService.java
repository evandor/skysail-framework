package io.skysail.server.text;

import io.skysail.api.text.*;
import aQute.bnd.annotation.component.Component;

/**
 * Using a low service ranking makes sure that this (default) service will
 * be the last in a chain of other TranslationRenderServices. It does not provide
 * any logic and will return the translation from some TranslationStore "as is",
 * without any substitutions.
 */
@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=" + PlainTranslationRenderService.SERVICE_RANKING})
public class PlainTranslationRenderService implements TranslationRenderService {

    public static final String SERVICE_RANKING = "-1";

    @Override
    public String render(String in, Object... substitutions) {
        return in.trim();
    }

    @Override
    public String render(Translation translation) {
        return translation.getValue();
    }

    @Override
    public boolean applicable(String unformattedTranslation) {
        return true;
    }

    @Override
    public String adjustText(String unformatted) {
        return unformatted;
    }

    @Override
    public String addRendererInfo() {
        return "";
    }

}
