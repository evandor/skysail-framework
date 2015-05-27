package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

import lombok.Getter;

public class ResourceModel<R extends SkysailServerResource<T>, T> {

    @Getter
    private R resource;

    private Class<?> parameterType;
    @Getter
    private List<Field> fields;
    private EntityModel entityModel;

    public ResourceModel(R resource) {
        this.resource = resource;
        parameterType = resource.getParameterType();
        fields = ReflectionUtils.getInheritedFields(resource.getParameterType());
        
        entityModel = new EntityModel(fields);
    }
    
    

}
