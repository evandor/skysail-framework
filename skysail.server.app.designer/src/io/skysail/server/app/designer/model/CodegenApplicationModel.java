package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.domain.core.ApplicationModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Extension augmenting the core application model with information needed
 * to create java classes from the model. 
 *
 */
@Getter
@ToString
@Slf4j
public class CodegenApplicationModel extends ApplicationModel {

    private final String packageName;
    private String path;
    private String projectName;

    public CodegenApplicationModel(Application appFromDb) {
        super(appFromDb.getName());
        this.packageName = appFromDb.getPackageName();
        this.path = appFromDb.getPath();
        this.projectName = appFromDb.getProjectName();
        setupModel(appFromDb);
    }

    public CodegenEntityModel addEntity(Entity entity) {
        log.info("CodegenApplicationModel: adding Entity '{}'", entity);
        CodegenEntityModel entityModel = new CodegenEntityModel(entity, packageName);
        add(entityModel);
        return entityModel;
    }

    private void setupModel(Application application) {
        application.getEntities().stream().forEach(entity -> {
            createEntityModel(entity);
            for (Entity subEntity : entity.getSubEntities()) {
                createEntityModel(subEntity);
            }
        });
    }

    private void createEntityModel(Entity dbEntity) {
        log.info("CodegenApplicationModel: adding Entity '{}'", dbEntity);
        CodegenEntityModel entityModel = new CodegenEntityModel(dbEntity, packageName);
        add(entityModel);
    }


}
