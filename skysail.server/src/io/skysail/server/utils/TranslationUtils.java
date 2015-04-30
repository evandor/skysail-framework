package io.skysail.server.utils;

import io.skysail.api.text.Translation;

import java.util.*;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import de.twenty11.skysail.server.app.TranslationRenderServiceHolder;

public class TranslationUtils {

    public static Optional<String> getBestTranslationMessage(List<TranslationRenderServiceHolder> translationRenderServices,
            Resource resource, String message) {
        List<TranslationRenderServiceHolder> sortedServices = getSortedTranslationRenderServices(translationRenderServices);

        return sortedServices
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
    
    public static Optional<Translation> getBestTranslation(List<TranslationRenderServiceHolder> translationRenderServices,
            Resource resource, String message) {
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


    private static List<TranslationRenderServiceHolder> getSortedTranslationRenderServices(List<TranslationRenderServiceHolder> translationRenderServices) {
        List<TranslationRenderServiceHolder> sortedServices = translationRenderServices.stream().sorted((t1, t2) -> {
            return t1.getServiceRanking().compareTo(t2.getServiceRanking());
        }).collect(Collectors.toList());
        return sortedServices;
    }
}
