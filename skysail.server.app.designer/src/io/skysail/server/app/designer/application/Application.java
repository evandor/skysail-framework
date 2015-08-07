package io.skysail.server.app.designer.application;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.forms.ListView;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString(of = { "id", "name" })
//@JsonPropertyOrder({ "title", "desc" })
public class Application implements Identifiable {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([a-zA-Z0-9_])*", message = "please choose a simpler Identifier. Some of the characters are not allowed.")
    private String name;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a simpler Identifier like 'skysail.server.app.designer.myapp'. Some of the characters are not allowed.")
    private String projectName;

    @Field
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a valid java package name like 'io.skysail.some.package'. Some of the provided characters are not allowed.")
    private String packageName;

    @Field
    @Size(min = 1)
    @NotNull
    private String path;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    //@Reference(cls = Entity.class)
    private List<Entity> entities;

    /**
     * A builder which sets the mandatory attributes
     */
    @Builder
    public Application(@NonNull String name, @NonNull String packageName, @NonNull String path, @NonNull String projectName) {
        this.name = name;
        this.packageName = packageName;
        this.path = path;
        this.projectName = projectName;
    }

    public List<Entity> getEntities() {
        if (entities == null) {
            entities = new ArrayList<>();
        }
        return entities;
    }

}
