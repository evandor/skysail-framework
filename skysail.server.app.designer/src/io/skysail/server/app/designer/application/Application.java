package io.skysail.server.app.designer.application;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.app.designer.entities.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String name;

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
