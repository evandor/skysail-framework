package io.skysail.server.utils;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.*;

@Slf4j
public class ResourceUtils {

    public static List<SkysailServerResource<?>> createSkysailServerResources(List<Class<? extends SkysailServerResource<?>>> entityServerResources,
            Resource resource) {

        List<SkysailServerResource<?>> result = new ArrayList<>();
        for (Class<? extends SkysailServerResource<?>> class1 : entityServerResources) {
            SkysailServerResource<?> newInstance;
            try {
                newInstance = class1.newInstance();
                newInstance.init(resource.getContext(), resource.getRequest(), resource.getResponse());
                newInstance.release();
                result.add(newInstance);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static SkysailServerResource<?> createSkysailServerResource(Class<? extends SkysailServerResource<?>> resourceClass,
            SkysailServerResource<?> resource) {
        SkysailServerResource<?> newInstance;
        try {
            newInstance = resourceClass.newInstance();
            newInstance.init(resource.getContext(), resource.getRequest(), resource.getResponse());
            newInstance.release();
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    public static Set<String> getSupportedMediaTypes(Resource resource, Class<?> type) {
        List<Variant> supportedVariants = Collections.emptyList();
        Set<MediaType> supportedMediaTypes = new HashSet<>();
        if (resource instanceof ServerResource) {
            supportedVariants = ((ServerResource) resource).getVariants();
            for (Variant variant : supportedVariants) {
                supportedMediaTypes.add(variant.getMediaType());
            }
        }

        Set<String> mediaTypes = new HashSet<String>();
        List<ConverterHelper> registeredConverters = Engine.getInstance().getRegisteredConverters();
        for (ConverterHelper ch : registeredConverters) {
            List<VariantInfo> variants;
            try {
                variants = ch.getVariants(type);
                if (variants == null) {
                    continue;
                }
                for (VariantInfo variantInfo : variants) {
                    if (supportedMediaTypes.contains(variantInfo.getMediaType())) {
                        String subType = variantInfo.getMediaType().getSubType();
                        mediaTypes.add(subType.equals("*") ? variantInfo.getMediaType().getName() : subType);
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return mediaTypes;
    }

    public static Locale determineLocale(SkysailServerResource<?> resource) {
        if (resource.getRequest() == null || resource.getRequest().getClientInfo() == null) {
            return Locale.getDefault();
        }
        List<Preference<Language>> acceptedLanguages = resource.getRequest().getClientInfo().getAcceptedLanguages();
        Locale localeToUse = Locale.getDefault();
        if (!acceptedLanguages.isEmpty()) {
            String[] languageSplit = acceptedLanguages.get(0).getMetadata().getName().split("-");
            if (languageSplit.length == 1) {
                localeToUse = new Locale(languageSplit[0]);
            } else if (languageSplit.length == 2) {
                localeToUse = new Locale(languageSplit[0], languageSplit[1]);
            }
        }
        return localeToUse;
    }


}
