package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@EqualsAndHashCode(of = { "entityName" })
@ToString
@Slf4j
public class EntityModel {

    private final String entityName;
    private final Set<FieldModel> fields = new HashSet<>();
    private final Set<ReferenceModel> references = new HashSet<>();
    private String className;
    private boolean rootEntity;
    private Optional<EntityModel> referencedBy;

    public EntityModel(@NonNull Entity entity) {
        this.entityName = entity.getName();
        rootEntity = entity.isRootEntity();
    }

    public void addField(EntityField f) {
        log.info("EntityModel:      adding Field '{}' to Entity '{}'", f.getName(), entityName);
        if (!fields.add(new FieldModel(f))) {
            throw new IllegalStateException("field '" + f.getName() + "' already exists!");
        }
    }

    public void addReference(Entity referencedEntity) {
        log.info("EntityModel:      adding Reference from Entity '{}' to Entity '{}'", entityName,
                referencedEntity.getName());
        if (!references.add(new ReferenceModel(this, referencedEntity))) {
            throw new IllegalStateException("reference '" + referencedEntity.getName() + "' already exists!");
        }
    }

    public void setClassName(String entityClassName) {
        this.className = entityClassName;
    }

    public boolean isRootEntity() {
        return rootEntity;
    }

    public void setReferencedBy(@NonNull EntityModel entityModel) {
        if (referencedBy.get() != null) {
            throw new IllegalStateException("setReferencedBy was called before on this object");
        }
        this.referencedBy = Optional.of(entityModel);
    }

}
