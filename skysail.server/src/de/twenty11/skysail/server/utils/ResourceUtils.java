package de.twenty11.skysail.server.utils;

import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.Resource;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

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
