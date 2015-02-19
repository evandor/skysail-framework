package io.skysail.server.text.store.git;

import io.skysail.api.text.TranslationStore;

import java.util.Locale;
import java.util.Optional;

import org.restlet.Request;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true)
public class GitTranslationStore implements TranslationStore {

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(key.replace(".", ""));
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
