package io.skysail.server.app.designer.entities;

import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.designer.fields.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(of = { "id", "name" })
public class DbEntity implements Identifiable {

    @Id
    private String id;

    @Field
    //@CheckValidJavaIdentifier
    @Pattern(regexp="[A-Z_$][a-zA-Z\\d_$]*", message = "Please start with an uppercase letter, and don't use any special characters.")
    @NotNull
    @Size(min=2)
    
    private String name;

    @Field(inputType = InputType.CHECKBOX)
    private boolean rootEntity;

    private List<DbEntityField> fields = new ArrayList<>();

    private List<ActionEntityField> actionFields;

    public List<ActionEntityField> getActionFields() {
        if (actionFields == null) {
            actionFields = new ArrayList<>();
        }
        return actionFields;
    }

    private List<DbEntity> subEntities;

    public List<DbEntity> getSubEntities() { // = getReferences?
        if (subEntities == null) {
            subEntities = new ArrayList<>();
        }
        return subEntities;
    }

    public DbEntity(String name) {
        this.name = name;
    }

}
