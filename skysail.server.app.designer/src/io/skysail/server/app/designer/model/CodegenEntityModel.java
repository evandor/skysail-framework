package io.skysail.server.app.designer.model;

import java.util.*;

import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.domain.core.EntityModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CodegenEntityModel extends EntityModel {

    private final Set<ActionFieldModel> actionFields = new HashSet<>();
    private final Set<ReferenceModel> references = new HashSet<>();
    private String className;
    private Optional<CodegenEntityModel> referencedBy;

    public CodegenEntityModel(Entity entityFromDb, String packageName) {
        super(packageName + "." + entityFromDb.getName());
        setAggregate(entityFromDb.isRootEntity());
        setupModel(entityFromDb);
    }

    private void setupModel(Entity entityFromDb) {
        entityFromDb.getFields().stream().forEach(fieldFromDb -> {
            createFieldModel(fieldFromDb);
        });
    }

    private void createFieldModel(EntityField fieldFromDb) {
        CodegenFieldModel fieldModel = addField(fieldFromDb);

//        dbEntity.getFields().stream().forEach(f -> {
//            entityModel.add(create(f));
//        });
//        
//        Collection<FieldModel> fields = entityModel.getFieldValues();//getFields(repo, dbEntity.getName(), application.getId());
//        
//        for (FieldModel fieldModel : fields) {
//            entityModel.addField(createEntityField(fieldModel));
//        }
    }

//    public void addField(EntityField f) {
//        log.info("CodegenEntityModel:      adding Field '{}' to Entity '{}'", f.getName(), f.getId());
//        if (getFields().get(f.getName()) != null) {
//            throw new IllegalStateException("field '" + f.getName() + "' already exists!");
//        }
//        getFields().put(f.getName(), new CodegenFieldModel(f));
//    }

    private CodegenFieldModel addField(EntityField fieldFromDb) {
        log.info("CodegenApplicationModel: adding Field '{}'", fieldFromDb);
        CodegenFieldModel fieldModel = new CodegenFieldModel(fieldFromDb);
        add(fieldModel);
        return fieldModel;
    }

    public void addActionField(ActionEntityField f) {
        log.info("CodegenEntityModel:      adding ActionField '{}' to Entity '{}'", f.getName(), f.getId());
        if (!actionFields.add(new ActionFieldModel(f))) {
            throw new IllegalStateException("actionField '" + f.getName() + "' already exists!");
        }
    }

    public void addReference(Entity referencedEntity) {
        log.info("CodegenEntityModel:      adding Reference from Entity '{}' to Entity '{}'", referencedEntity.getId(),
                referencedEntity.getName());
        if (!references.add(new ReferenceModel(this, referencedEntity))) {
            throw new IllegalStateException("reference '" + referencedEntity.getName() + "' already exists!");
        }
    }

    public void setClassName(String entityClassName) {
        this.className = entityClassName;
    }
    
    public void setReferencedBy(@NonNull CodegenEntityModel entityModel) {
        if (referencedBy != null && referencedBy.get() != null) {
            throw new IllegalStateException("setReferencedBy was called before on this object");
        }
        this.referencedBy = Optional.of(entityModel);
    }


}
