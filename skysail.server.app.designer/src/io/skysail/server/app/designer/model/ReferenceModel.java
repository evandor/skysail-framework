package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.entities.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class ReferenceModel {

    private final String name;

    public ReferenceModel(@NonNull Entity entity) {
        this.name = entity.getName();
    }
    
    public String getVariableName() {
        return name.substring(0,1).toLowerCase().concat(name.substring(1));
    }

}
