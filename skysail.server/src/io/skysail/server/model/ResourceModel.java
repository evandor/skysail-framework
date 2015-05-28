package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import lombok.Getter;

public class ResourceModel<R extends SkysailServerResource<T>, T> {

    @Getter
    private R resource;

    private Class<?> parameterType;
    private EntityModel entityModel;

    public ResourceModel(R resource) {
        this.resource = resource;
        parameterType = resource.getParameterType();
        List<Field> fields = ReflectionUtils.getInheritedFields(resource.getParameterType());
        entityModel = new EntityModel(fields);
    }

    public Map<String, Object> dataFromMap(Map<String, Object> props) {
        return entityModel.dataFromMap(props , resource);
    }
    
   

}
