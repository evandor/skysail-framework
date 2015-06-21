package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.fields.EntityField;
import lombok.*;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class FieldModel {

    private String name;

    public FieldModel(EntityField f) {
        this.name = f.getName();
    }

}
