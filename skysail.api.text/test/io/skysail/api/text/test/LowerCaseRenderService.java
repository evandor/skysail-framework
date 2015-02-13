package io.skysail.api.text.test;

import io.skysail.api.text.RenderService;
import io.skysail.api.text.Store;
import io.skysail.api.text.Translation;

import java.util.Optional;

import lombok.Getter;

import org.restlet.Request;

public class LowerCaseRenderService implements RenderService {

    public class DefaultTranslation implements Translation {

        @Getter
        private String translation;

        public DefaultTranslation(String text) {
            this.translation = text;
        }

    }

    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request, Store store) {
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
        return translation.getTranslation().toLowerCase();
    }

}
