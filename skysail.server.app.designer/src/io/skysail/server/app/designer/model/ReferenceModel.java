package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.entities.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "referencedEntityName")
@ToString
public class ReferenceModel {

    private final String referencedEntityName;
    private EntityModel referencedBy;

    public ReferenceModel(@NonNull EntityModel entityModel, @NonNull Entity referencedEntity) {
        this.referencedEntityName = referencedEntity.getName();
        this.referencedBy = entityModel;
    }
    
    public String getVariableName() {
        return referencedEntityName.substring(0,1).toLowerCase().concat(referencedEntityName.substring(1));
    }

}
