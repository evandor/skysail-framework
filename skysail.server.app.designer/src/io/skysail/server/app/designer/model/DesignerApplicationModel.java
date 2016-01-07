package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.domain.core.ApplicationModel;
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
public class DesignerApplicationModel extends ApplicationModel {

    private final String packageName;
    private String path;
    private String projectName;

    public DesignerApplicationModel(DbApplication appFromDb) {
        super(appFromDb.getName());
        this.packageName = appFromDb.getPackageName();
        this.path = appFromDb.getPath();
        this.projectName = appFromDb.getProjectName();
        setupModel(appFromDb);
    }

    public DesignerEntityModel addEntity(DbEntity entity) {
        log.info("DesignerApplicationModel: adding DbEntity '{}'", entity);
        DesignerEntityModel entityModel = new DesignerEntityModel(entity, packageName);
        addOnce(entityModel);
        return entityModel;
    }

    private void setupModel(DbApplication application) {
        application.getEntities().stream().forEach(entity -> {
            createEntityModel(entity);
            for (DbEntity subEntity : entity.getSubEntities()) {
                createEntityModel(subEntity);
            }
        });
    }

    private void createEntityModel(DbEntity dbEntity) {
        log.info("DesignerApplicationModel: adding DbEntity '{}'", dbEntity);
        DesignerEntityModel entityModel = new DesignerEntityModel(dbEntity, packageName);
        addOnce(entityModel);
    }


}
