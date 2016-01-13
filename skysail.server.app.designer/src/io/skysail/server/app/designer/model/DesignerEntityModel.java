package io.skysail.server.app.designer.model;

import java.util.*;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.*;
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

    private final Set<ActionFieldModel> actionFields = new HashSet<>();

    // links to other entities
    private final Set<ReferenceModel> references = new HashSet<>();  
    
    private Optional<DesignerEntityModel> referencedBy;

    private String className;

    public DesignerEntityModel(DbEntity entityFromDb, String packageName) {
        super(packageName + "." + entityFromDb.getName());
        setAggregate(entityFromDb.isRootEntity());
        setupModel(entityFromDb);
    }

    public void addActionField(ActionEntityField f) {
        log.info("DesignerEntityModel:      adding ActionField '{}' to DbEntity '{}'", f.getName(), f.getId());
        if (!actionFields.add(new ActionFieldModel(f))) {
            throw new IllegalStateException("actionField '" + f.getName() + "' already exists!");
        }
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
    
    private void setupModel(DbEntity entityFromDb) {
        entityFromDb.getFields().stream().forEach(this::addField);
    }

    private void addField(DbEntityField fieldFromDb) { // NOSONAR
        log.info("DesignerApplicationModel: adding Field '{}'", fieldFromDb);
        DesignerFieldModel fieldModel = new DesignerFieldModel(fieldFromDb);
        add(fieldModel);
    }

}
