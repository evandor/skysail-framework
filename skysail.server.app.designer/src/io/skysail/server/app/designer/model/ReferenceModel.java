package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.entities.Entity;
import lombok.*;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class ReferenceModel {

    private String name;

    public ReferenceModel(Entity entity) {
        this.name = entity.getName();
    }

}
