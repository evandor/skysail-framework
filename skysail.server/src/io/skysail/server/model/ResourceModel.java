package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import de.twenty11.skysail.server.core.FormField;

/**
 * The model of the resource from which the representation is derived.
 * 
 * <p>
 * Contrary to (e.g.) a JSON representation of an entity, an HTML representation... TODO  
 * </p>
 *
 * @param <R>
 * @param <T>
 */
@Slf4j
@Getter
@ToString
public class ResourceModel<R extends SkysailServerResource<T>, T> {

    private R resource;
    private Class<?> parameterType;
    private EntityModel entityModel;
    private String title = "Skysail";
    private Object source;
    private List<FormField> formfields;

    public ResourceModel(R resource, Object source) {
        this.resource = resource;
        this.source = source;
        parameterType = resource.getParameterType();
        List<Field> fields = ReflectionUtils.getInheritedFields(resource.getParameterType());
        entityModel = new EntityModel(fields);
        
        determineFormfields(resource, source);
    }

    public Map<String, Object> dataFromMap(Map<String, Object> props) {
        return entityModel.dataFromMap(props , resource);
    }

    private void determineFormfields(R resource, Object source) {
        FieldFactory fieldFactory = FieldsFactory.getFactory(source, resource);
        log.info("using factory '{}' for {}-Source: {}", new Object[] { fieldFactory.getClass().getSimpleName(),
                source.getClass().getSimpleName(), source });
        try {
            formfields = fieldFactory.determineFrom(resource);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    
   

}
