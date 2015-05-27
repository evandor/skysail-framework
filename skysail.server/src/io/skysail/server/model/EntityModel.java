package io.skysail.server.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityModel {

    private List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

    public EntityModel(List<Field> fields) {
        fields.stream().forEach(field -> {
            fieldDescriptors .add(new FieldDescriptor(field));
        });
    }

}
