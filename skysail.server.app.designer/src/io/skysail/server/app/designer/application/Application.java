package io.skysail.server.app.designer.application;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.forms.ListView;

import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

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
    @Pattern(regexp = "[a-zA-Z_]([\\.\\w])*", message = "please choose a simpler Identifier. Some of the characters are not allowed.")
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
    
    @Field(type = InputType.READONLY)
    @ListView(hide = true)
    private String owner;
    
    //@Reference(cls = Entity.class)
    private List<Entity> entities;

    public Application(String name) {
        this.name = name;
    }

    public List<Entity> getEntities() {
        if (entities == null) {
            entities = new ArrayList<>();
        }
        return entities;
    }

}
