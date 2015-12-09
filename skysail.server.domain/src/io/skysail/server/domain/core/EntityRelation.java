package io.skysail.server.domain.core;

import lombok.*;

@Getter
@ToString
public class EntityRelation {

    private EntityModel targetEntityModel;
    private EntityRelationType type;

    public EntityRelation(EntityModel targetEntityModel, EntityRelationType type) {
        this.targetEntityModel = targetEntityModel;
        this.type = type;
    }

}
