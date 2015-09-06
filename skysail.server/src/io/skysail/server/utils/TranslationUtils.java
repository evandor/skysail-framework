package io.skysail.server.utils;

import io.skysail.api.text.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.text.TranslationStoreHolder;

import java.util.*;
import java.util.stream.Collectors;

import de.twenty11.skysail.server.app.TranslationRenderServiceHolder;

public class TranslationUtils {

    public static Optional<Translation> getBestTranslation(Set<TranslationStoreHolder> stores, String key,
            SkysailServerResource<?> resource) {
        return getSortedTranslationStores(stores).stream().filter(store -> {
            return store.getStore().get() != null;
        }).map(store -> {
            return createFromStore(key, resource, store, stores);
        }).filter(t -> {
            return t != null;
        }).findFirst();
    }

    public static List<Translation> getAllTranslations(Set<TranslationStoreHolder> stores, String key, SkysailServerResource<?> resource) {
        List<TranslationStoreHolder> sortedTranslationStores = getSortedTranslationStores(stores);
        return sortedTranslationStores.stream().filter(store -> {
            return store.getStore().get() != null;
        }).map(store -> {
            return createFromStore(key, resource, store, stores);
        }).filter(t -> {
            return t != null;
        }).collect(Collectors.toList());
    }

    public static Translation getTranslation(String key, Set<TranslationStoreHolder> stores, String selectedStore,
            SkysailServerResource<?> resource) {
        Optional<TranslationStoreHolder> storeToUse = stores.stream().filter(s -> {
            return s.getStore().get().getClass().getName().equals(selectedStore);
        }).findFirst();
        if (storeToUse.isPresent()) {
            return createFromStore(key, resource, storeToUse.get(), stores);
        }
        return null;
    }

    public static String render(Set<TranslationRenderServiceHolder> translationRenderServices, Translation translation) {
        List<TranslationRenderServiceHolder> sortedTranslationRenderServices = getSortedTranslationRenderServices(translationRenderServices);

        return sortedTranslationRenderServices.stream().filter(renderService -> {
            return renderService.getService().get().applicable(translation.getValue());
        }).map(renderService -> {
            return renderService.getService().get().render(translation);
        }).findFirst().orElse("");
    }

    private static List<TranslationRenderServiceHolder> getSortedTranslationRenderServices(
            Set<TranslationRenderServiceHolder> translationRenderServices) {
        List<TranslationRenderServiceHolder> sortedServices = translationRenderServices.stream().sorted((t1, t2) -> {
            return t2.getServiceRanking().compareTo(t1.getServiceRanking());
        }).collect(Collectors.toList());
        return sortedServices;
    }

    private static List<TranslationStoreHolder> getSortedTranslationStores(Set<TranslationStoreHolder> stores) {
        List<TranslationStoreHolder> sortedStores = stores.stream().sorted((t1, t2) -> {
            return t1.getServiceRanking().compareTo(t2.getServiceRanking());
        }).collect(Collectors.toList());
        return sortedStores;
    }

    private static Translation createFromStore(String key, SkysailServerResource<?> resource, TranslationStoreHolder store,
            Set<TranslationStoreHolder> stores) {
        String result = store.getStore().get().get(key, resource.getClass().getClassLoader(), resource.getRequest())
                .orElse(null);
        if (result == null) {
            return null;
        }
        if (resource instanceof I18nArgumentsProvider) {
            MessageArguments messageArguments = ((I18nArgumentsProvider)resource).getMessageArguments();
            return new Translation(
                    result,
                    store.getStore().get(),
                    messageArguments.get(key),
                    stores.stream().map(TranslationStoreHolder::getName).collect(Collectors.toSet()));
        }
        return new Translation(
                result,
                store.getStore().get(),
                stores.stream().map(TranslationStoreHolder::getName).collect(Collectors.toSet()));
    }

}
