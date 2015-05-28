package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Field;
import java.util.*;

public class EntityModel {

    private List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

    public EntityModel(List<Field> fields) {
        fields.stream().forEach(field -> {
            fieldDescriptors .add(new FieldDescriptor(field));
        });
    }

    public Map<String, Object> dataFromMap(Map<String, Object> props, SkysailServerResource<?> resource) {
        Map<String,Object> result = new HashMap<>();
        fieldDescriptors.stream().forEach(f -> {
           result.putAll(f.dataFromMap(props, resource));  
        });
        return result;
    }
    
    

}
