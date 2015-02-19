package io.skysail.api.text.test;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.api.text.TranslationStore;

import java.util.Optional;

import org.restlet.Request;

public class LowerCaseRenderService implements TranslationRenderService {

    public class DefaultTranslation extends Translation {

        public DefaultTranslation(String text) {
            super(Optional.of(text), 1000);
        }

        @Override
        public Class<? extends TranslationRenderService> getTranslatedBy() {
            return LowerCaseRenderService.class;
        }

    }

    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request, TranslationStore store) {
        if (store != null) {
            Optional<String> text = store.get(key);
            if (text.isPresent()) {
                return new DefaultTranslation(text.get());
            }
            return new DefaultTranslation("");
        }
        return new DefaultTranslation(key);
    }

    @Override
    public String render(Translation translation, Object... substitutions) {
        return translation.getValue().toLowerCase();
    }

    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request) {
        return null;
    }

}
