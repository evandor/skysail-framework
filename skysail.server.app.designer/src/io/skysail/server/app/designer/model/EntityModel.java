package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;

import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@EqualsAndHashCode(of={"entityName"})
@ToString
@Slf4j
public class EntityModel {

    private String entityName;
    private Set<FieldModel> fields = new HashSet<>();
    private Set<ReferenceModel> references = new HashSet<>();

    public EntityModel(String entityName) {
        this.entityName = entityName;
    }

    public void addField(EntityField f) {
        log.info("EntityModel:      adding Field '{}' to Entity '{}'", f.getName(), entityName);
        if (!fields.add(new FieldModel(f))) {
            throw new IllegalStateException("field '"+f.getName()+"' already exists!");
        }
    }

    public void addReference(Entity entity) {
        log.info("EntityModel:      adding Reference (to Entity '{}') to Entity '{}'", entity.getName(), entityName);
        if (!references.add(new ReferenceModel(entity))) {
            throw new IllegalStateException("field '"+entity.getName()+"' already exists!");
        }
    }

}
