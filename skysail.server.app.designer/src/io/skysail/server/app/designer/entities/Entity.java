package io.skysail.server.app.designer.entities;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.designer.fields.EntityField;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(of = { "id", "name" })
public class Entity implements Identifiable {
    
    @Id
    private String id;

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
