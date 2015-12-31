package io.skysail.server.app.designer.entities;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.skysail.domain.*;
import io.skysail.domain.html.*;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.forms.ListView;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "name", "fields", "subEntities" })
public class DbEntity implements Identifiable, Nameable, Serializable {

    private static final long serialVersionUID = 7571240311935363328L;

    @Id
    private String id;

    @Field
    //@CheckValidJavaIdentifier
    @Pattern(regexp="[A-Z_$][a-zA-Z\\d_$]*", message = "Please start with an uppercase letter, and don't use any special characters.")
    @NotNull
    @Size(min=2)
    @ListView(link = FieldsResource.class, truncate = 20)
    private String name;

    @Field(inputType = InputType.CHECKBOX)
    private boolean rootEntity = true;

//    @Relation
    @JsonIgnore
    private DbApplication dbApplication;

    @Relation
    private List<DbEntityField> fields = new ArrayList<>();

    private List<ActionEntityField> actionFields;

    public List<ActionEntityField> getActionFields() {
        if (actionFields == null) {
            actionFields = new ArrayList<>();
        }
        return actionFields;
    }

    private List<DbEntity> subEntities = new ArrayList<>();

    public DbEntity(@NonNull String name) {
        this.name = name;
    }
    
    @Builder
    public DbEntity(@NonNull String name, boolean rootEntity) {
        this.name = name;
        this.rootEntity = rootEntity;
    }

}
