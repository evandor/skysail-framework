package io.skysail.server.text;

import io.skysail.api.text.Translation;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.api.text.TranslationStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.Request;

public abstract class AbstractTranslationRenderService implements TranslationRenderService {

    protected List<TranslationStoreHolder> stores = new ArrayList<>();

    protected abstract Translation createTranslation(Optional<String> t);

    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request, TranslationStore store) {
        return createTranslation(store.get(key, cl, request));
    }

    /**
     * Here, no specific store is provided, so the available translation stores
     * are sorted by their service ranking and asked for a translation.
     */
    @Override
    public Translation getTranslation(String key, ClassLoader cl, Request request) {

        List<TranslationStoreHolder> sortedStores = stores.stream().sorted((t1, t2) -> {
            return t1.getServiceRanking().compareTo(t2.getServiceRanking());
        }).collect(Collectors.toList());

        Optional<Translation> bestTranslation = sortedStores.stream().filter(store -> {
            return store.getStore().get() != null;
        }).map(store -> {
            return store.getStore().get().get(key, cl, request);
        }).map(translation -> {
            return createTranslation(translation);
        }).findFirst();

        if (bestTranslation.isPresent()) {
            return bestTranslation.get();
        }
        return null;

    }
}
