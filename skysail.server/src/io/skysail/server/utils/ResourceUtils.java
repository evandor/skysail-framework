package io.skysail.server.utils;

import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.Resource;

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

}