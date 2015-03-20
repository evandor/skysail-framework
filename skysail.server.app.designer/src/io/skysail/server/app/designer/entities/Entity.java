package io.skysail.server.app.designer.entities;

import io.skysail.server.app.designer.fields.EntityField;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity {

    @io.skysail.api.forms.Field
    private String name;

    private List<EntityField> fields;

    public List<EntityField> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

}
