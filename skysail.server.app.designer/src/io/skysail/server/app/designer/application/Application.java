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
    private String name;
    
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
