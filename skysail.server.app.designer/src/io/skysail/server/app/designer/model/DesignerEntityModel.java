package io.skysail.server.app.designer.model;

import java.util.*;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Extension augmenting the core entity model with information to create java
 * entities from the model.
 *
 */
@Getter
@Slf4j
public class DesignerEntityModel extends EntityModel {

    // links to other entities
    private final Set<ReferenceModel> references = new HashSet<>();

    private Optional<DesignerEntityModel> referencedBy;

    private String className;

    public DesignerEntityModel(DbEntity entityFromDb, String packageName) {
        super(packageName + "." + entityFromDb.getName().toLowerCase() + "." + entityFromDb.getName());
        setAggregate(entityFromDb.isRootEntity());
        setupFieldModels(entityFromDb);
    }

    public void addReference(DbEntity referencedEntity) {
        log.info("DesignerEntityModel:      adding Reference from DbEntity '{}' to DbEntity '{}'",
                referencedEntity.getId(), referencedEntity.getName());
        if (!references.add(new ReferenceModel(this, referencedEntity))) {
            throw new IllegalStateException("reference '" + referencedEntity.getName() + "' already exists!");
        }
    }

    public void setClassName(String entityClassName) {
        this.className = entityClassName;
    }

    public void setReferencedBy(@NonNull DesignerEntityModel entityModel) {
        if (referencedBy != null && referencedBy.get() != null) {
            throw new IllegalStateException("setReferencedBy was called before on this object");
        }
        this.referencedBy = Optional.of(entityModel);
    }

    public boolean hasSelfReference() {
        return getRelations().stream().filter(r -> r.getTargetEntityModel().equals(this)).findFirst().isPresent();
    }

    private void setupFieldModels(DbEntity entityFromDb) {
        entityFromDb.getFields().stream().forEach(this::addField);
    }

    private void addField(DbEntityField fieldFromDb) { // NOSONAR
        log.info("DesignerApplicationModel: adding Field '{}'", fieldFromDb);
        DesignerFieldModel fieldModel = new DesignerFieldModel(this, fieldFromDb);
        add(fieldModel);
    }

}
