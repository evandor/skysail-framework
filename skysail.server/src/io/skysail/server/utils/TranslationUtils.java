package io.skysail.server.utils;

import io.skysail.api.text.Translation;
import io.skysail.server.text.TranslationStoreHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import de.twenty11.skysail.server.app.TranslationRenderServiceHolder;

public class TranslationUtils {
    
    public static Optional<String> getFromStores(List<TranslationStoreHolder> stores, String key,Resource resource) {
        List<TranslationStoreHolder> sortedTranslationStores = getSortedTranslationStores(stores);
        return sortedTranslationStores
                .stream()
                .filter(store -> {
                    return store.getStore().get() != null;
                })
                .map(store -> {
                    return store.getStore().get().get(key, resource.getClass().getClassLoader(), resource.getRequest()).orElse(null);
                }).filter(t -> {return t != null;}).findFirst();
    }

    public static Optional<String> getBestTranslationMessage(
            List<TranslationRenderServiceHolder> translationRenderServices, Resource resource, String message) {
        List<TranslationRenderServiceHolder> sortedTranslationRenderServices = getSortedTranslationRenderServices(translationRenderServices);

        return sortedTranslationRenderServices
                .stream()
                .filter(service -> {
                    return service.getService().get() != null;
                })
                .map(service -> {
                    Translation translation = service.getService().get()
                            .getTranslation(message, resource.getClass().getClassLoader(), resource.getRequest());
                    return service.getService().get().render(translation);
                }).filter(t -> {
                    return t != null;
                }).findFirst();
    }

    public static Optional<Translation> getBestTranslation(
            List<TranslationRenderServiceHolder> translationRenderServices, Resource resource, String message) {
        List<TranslationRenderServiceHolder> sortedServices = getSortedTranslationRenderServices(translationRenderServices);

        return sortedServices
                .stream()
                .filter(service -> {
                    return service.getService().get() != null;
                })
                .map(service -> {
                    return service.getService().get()
                            .getTranslation(message, resource.getClass().getClassLoader(), resource.getRequest());
                }).filter(t -> {
                    return t != null;
                }).findFirst();

    }

    private static List<TranslationRenderServiceHolder> getSortedTranslationRenderServices(
            List<TranslationRenderServiceHolder> translationRenderServices) {
        List<TranslationRenderServiceHolder> sortedServices = translationRenderServices.stream().sorted((t1, t2) -> {
            return t2.getServiceRanking().compareTo(t1.getServiceRanking());
        }).collect(Collectors.toList());
        return sortedServices;
    }

    private static List<TranslationStoreHolder> getSortedTranslationStores(List<TranslationStoreHolder> stores) {
        List<TranslationStoreHolder> sortedStores = stores.stream().sorted((t1, t2) -> {
            return t1.getServiceRanking().compareTo(t2.getServiceRanking());
        }).collect(Collectors.toList());
        return sortedStores;
    }

}
