package io.skysail.server.utils;

import io.skysail.server.restlet.resources.EntityServerResource;

import java.io.IOException;
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

    public static EntityServerResource<?> createEntityServerResource(Class<? extends EntityServerResource<?>> entityServerResource,
            Resource resource) {
        EntityServerResource<?> newInstance;
        try {
            newInstance = entityServerResource.newInstance();
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
