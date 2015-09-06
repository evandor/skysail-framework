package io.skysail.api.text.test;

import io.skysail.api.text.*;

public class LowerCaseRenderService implements TranslationRenderService {

    public class DefaultTranslation extends Translation {

        public DefaultTranslation(String text) {
            super(text, null,null);
        }

    }

    @Override
    public String render(String in, Object... substitutions) {
        return in.toLowerCase();
    }

    @Override
    public String render(Translation translation) {
        return translation.getValue().toLowerCase();
    }

    @Override
    public boolean applicable(String unformattedTranslation) {
        return true;
    }

    @Override
    public String adjustText(String unformatted) {
        return null;
    }

    @Override
    public String addRendererInfo() {
        return null;
    }

}
