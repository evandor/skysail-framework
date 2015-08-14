package io.skysail.server.app.designer.entities;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.designer.fields.ActionEntityField;

import java.util.*;

import javax.persistence.Id;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(of = { "id", "name" })
public class Entity implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

    @Field(inputType = InputType.CHECKBOX)
    private boolean rootEntity;

    private List<String> fields;

    public List<String> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    private List<ActionEntityField> actionFields;

    public List<ActionEntityField> getActionFields() {
        if (actionFields == null) {
            actionFields = new ArrayList<>();
        }
        return actionFields;
    }

    private List<Entity> subEntities;

    public List<Entity> getSubEntities() { // = getReferences?
        if (subEntities == null) {
            subEntities = new ArrayList<>();
        }
        return subEntities;
    }

    public Entity(String name) {
        this.name = name;
    }

}
