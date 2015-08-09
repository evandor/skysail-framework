package io.skysail.server.documentation;

import io.skysail.server.restlet.resources.ListServerResource;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class EntitiesResource extends ListServerResource<EntityDescriptor> {

    public EntitiesResource() {
        //super(EntityResource.class);
        setDescription("Provides information about the RESTful Entities of this application");
    }

    @Override
    public List<EntityDescriptor> getEntity() {
        Set<Class<?>> classes = getApplication().getRoutesMap().values().stream()
                .map(routeBuilder -> routeBuilder.getTargetClass()).filter(cls -> cls != null)
                .collect(Collectors.toSet());
        Set<String> entities = classes.stream()
                .map(cls -> parameterType(cls))
                .map(cls -> getEntityType(cls))
                .collect(Collectors.toSet());
        return entities.stream()
                .map(name -> new EntityDescriptor(name))
                .collect(Collectors.toList());
    }

    private Class<?> parameterType(Class<?> cls) {
        ParameterizedType parameterizedType = getParameterizedType(cls);
        Type firstActualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (firstActualTypeArgument.getTypeName().startsWith("java.util.Map")) {
            return Map.class;
        }
        return (Class<?>) firstActualTypeArgument;
    }

    private ParameterizedType getParameterizedType(Class<?> cls) {
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        }
        return getParameterizedType(cls.getSuperclass());
    }

    private String getEntityType(Class<?> ssr) {
        return ssr.toString().split(" ")[1].trim();
    }

}
