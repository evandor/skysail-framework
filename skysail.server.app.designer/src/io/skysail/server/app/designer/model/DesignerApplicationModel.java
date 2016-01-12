package io.skysail.server.app.designer.model;

import java.util.List;
import java.util.Optional;

import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.EntityRelation;
import io.skysail.domain.core.EntityRelationType;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Extension augmenting the core application model with information needed
 * to create java classes from the model.
 *
 */
@Getter
@Slf4j
public class DesignerApplicationModel extends ApplicationModel {

    private final String packageName;
    private String path;
    private String projectName;

    /**
     * Creates a DesignerApplicationModel from a persisted model.
     */
    public DesignerApplicationModel(DbApplication appFromDb) {
        super(appFromDb.getName());
        this.packageName = appFromDb.getPackageName();
        this.path = appFromDb.getPath();
        this.projectName = appFromDb.getProjectName();
        setupModel(appFromDb);
    }

//    public DesignerEntityModel addEntity(DbEntity entity) {
//        log.info("DesignerApplicationModel: adding DbEntity '{}'", entity);
//        DesignerEntityModel entityModel = new DesignerEntityModel(entity, packageName);
//        addOnce(entityModel);
//        return entityModel;
//    }
    
    @Override
    public String toString() {
        String addedInfo = 
                new StringBuilder(", projectName=").append(projectName)
                          .append(", path=").append(path).append("\n").toString();
        return super.toString().replaceFirst("\\n", addedInfo);
    }
    
    private void setupModel(DbApplication dbApplication) {
        setupEntities(dbApplication);
        setupRelations(dbApplication);
    }

    private void setupEntities(DbApplication application) {
        application.getEntities().stream().forEach(this::createEntityModel);
    }

    private void createEntityModel(DbEntity dbEntity) { // NOSONAR
        log.info("DesignerApplicationModel: adding DbEntity '{}'", dbEntity);
        DesignerEntityModel entityModel = new DesignerEntityModel(dbEntity, packageName);
        addOnce(entityModel);
    }

    private void setupRelations(DbApplication dbApplication) {
        dbApplication.getEntities().stream().forEach(dbEntity -> {
            List<DbEntity> oneToManyRelations = dbEntity.getOneToManyRelations();
            Optional<EntityModel> sourceEntityModel = getEntityModel(dbEntity.getName());
            if (!sourceEntityModel.isPresent()) {
                log.error("error finding entityModel with name '{}'", dbEntity.getName());
                return;
            }
            oneToManyRelations.stream().forEach(oneToManyRelation -> {
                String relationName = oneToManyRelation.getName().substring(0, 1).toLowerCase() + oneToManyRelation.getName().substring(1) + "s";
                Optional<EntityModel> targetEntityModel = getEntityModel(oneToManyRelation.getName());
                if (!targetEntityModel.isPresent()) {
                    log.error("error finding entityModel with name '{}'", oneToManyRelation.getName());
                    return;
                }
                EntityRelation newRelation = new EntityRelation(relationName, targetEntityModel.get(), EntityRelationType.ONE_TO_MANY);
                sourceEntityModel.get().getRelations().add(newRelation);
            });
        });
    }

    private Optional<EntityModel> getEntityModel(String dbEntityName) {
        return getEntityValues().stream().filter(entity -> 
            entity.getSimpleName().equals(dbEntityName)
        ).findFirst();
    }


}
