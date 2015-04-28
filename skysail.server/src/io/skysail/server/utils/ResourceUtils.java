package io.skysail.server.utils;

import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.restlet.resource.ServerResource;

@Slf4j
public class ResourceUtils {

    public static List<EntityServerResource<?>> createEntityServerResources(List<Class<? extends EntityServerResource<?>>> entityServerResources,
            Resource resource) {
        
        List<EntityServerResource<?>> result = new ArrayList<>();
        for (Class<? extends EntityServerResource<?>> class1 : entityServerResources) {
            EntityServerResource<?> newInstance;
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
    public static Set<String> getSupportedMediaTypes(Resource resource, Object entity) {
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
                variants = ch.getVariants(entity.getClass());
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

  

}
