package io.skysail.server.text;

import io.skysail.api.text.TranslationStore;

import java.util.Optional;

import lombok.Getter;

@Getter
public class StoreAndTranslation {
    
    private TranslationStore store;
    private Optional<String> translation;

    public StoreAndTranslation(TranslationStore store, Optional<String> translation) {
        this.store = store;
        this.translation = translation;
    }

}
