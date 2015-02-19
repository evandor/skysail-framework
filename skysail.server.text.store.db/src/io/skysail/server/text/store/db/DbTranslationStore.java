package io.skysail.server.text.store.db;

import io.skysail.api.text.TranslationStore;
import io.skysail.server.text.store.db.text.Text;

import java.util.Locale;
import java.util.Optional;

import org.restlet.Request;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true)
public class DbTranslationStore implements TranslationStore {

    @Override
    public Optional<String> get(String key) {
        Text text = TextRepository.getInstance().getById(key);
        return Optional.of(text.getValue());
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl) {
        return get(key);
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request) {
        return get(key);
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
        return get(key);
    }

}
