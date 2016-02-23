package io.skysail.server.app.designer.entities;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.*;

import io.skysail.domain.*;
import io.skysail.domain.html.*;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.db.validators.UniqueNameForParent;
import io.skysail.server.forms.ListView;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "name", "fields" })
@UniqueNameForParent(entityClass = DbEntity.class, parent = "dbApplication", relationName = "entities")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
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

    @JsonBackReference
    private DbApplication dbApplication;

    @Relation
    @JsonManagedReference
    private List<DbEntityField> fields = new ArrayList<>();

    @JsonBackReference
    private DbEntity parent;
    
    @Relation
    private List<DbEntity> oneToManyRelations = new ArrayList<>();

    public DbEntity(@NonNull String name) {
        this.name = name;
    }
    
    @Builder
    public DbEntity(@NonNull String name, boolean rootEntity) {
        this.name = name;
        this.rootEntity = rootEntity;
    }

}
