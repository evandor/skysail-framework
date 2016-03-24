package io.skysail.server.app.designer.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.valueobjects.DbValueObject;
import io.skysail.server.db.validators.UniqueName;
import io.skysail.server.forms.ListView;
import io.skysail.server.forms.PostView;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = { "id", "name", "entities" })
@UniqueName(entityClass = DbApplication.class)
public class DbApplication implements Nameable, Serializable {

    private static final long serialVersionUID = -7673527765463838726L;

    @Id
    private String id;

    @Field
    private ApplicationStatus status = ApplicationStatus.UNDEFINED;
    
    @Field
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "[a-zA-Z_]([a-zA-Z0-9_])*", message = "please choose a simpler Identifier. Some of the characters are not allowed.")
    @ListView(link = EntitiesResource.class, truncate = 40)
    @PostView(tab = "newApp")
    private String name;

    @Field(inputType = InputType.TEXTAREA)
    @Size(max = 1000)
    @ListView(hide=true)
    @PostView(tab = "newApp")
    private String description;

    @Field
    //@Pattern(regexp = "[a-z_.]*", message = "packages should be lowercase with dots and underscores only")
    @ListView(hide = true)
    @PostView(tab = "details")
    private String projectName;

    @Field
    @Pattern(regexp = "[a-z_.]*", message = "packages should be lowercase with dots and underscores only")
    //@ListView(hide = true)
    @PostView(tab = "details")
    private String packageName;

    @Field
    @ListView(hide = true)
    @PostView(tab = "details")
    private String path;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    @Relation
    @JsonManagedReference
    private List<DbEntity> entities = new ArrayList<>();

    @Relation
    @JsonManagedReference
    private List<DbValueObject> valueObjects = new ArrayList<>();

    /**
     * A builder which sets the mandatory attributes
     */
    @Builder
    public DbApplication(@NonNull String name, @NonNull String packageName, @NonNull String path, @NonNull String projectName) {
        this.name = name;
        this.packageName = packageName;
        this.path = path;
        this.projectName = projectName;
    }

}
