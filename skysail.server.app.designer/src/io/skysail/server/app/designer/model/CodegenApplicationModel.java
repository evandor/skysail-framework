package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.domain.core.ApplicationModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class CodegenApplicationModel extends ApplicationModel {

    private final String applicationName;
    private final String packageName;
    private String path;
    private String projectName;

    public CodegenApplicationModel(Application appFromDb, DesignerRepository repo) {
        super(appFromDb.getName());
        this.applicationName = appFromDb.getName();
        this.packageName = appFromDb.getPackageName();
        path = appFromDb.getPath();
        projectName = appFromDb.getProjectName();
        setupModel(appFromDb, repo);
        validate();
    }

    private void setupModel(Application application, DesignerRepository repo) {
        createEnityModels(application, repo);
    }

    private void createEnityModels(Application application, DesignerRepository repo) {
        application.getEntities().stream().forEach(entity -> {
            createEntityModel(application, repo, entity);
            for (Entity subEntity : entity.getSubEntities()) {
                createEntityModel(application, repo, subEntity);
            }
        });
    }

    private void createEntityModel(Application application, DesignerRepository repo, Entity dbEntity) {
        addEntity(dbEntity);
    }

    public CodegenEntityModel addEntity(Entity entity) {
        log.info("CodegenApplicationModel: adding Entity '{}'", entity);
        CodegenEntityModel entityModel = new CodegenEntityModel(entity, packageName);
        add(entityModel);
        return entityModel;
    }

    public void validate() {
       // eachEntitiesReferencesMustPointToExistingEntity();
    }
//
//    public CodegenEntityModel getEntityModel(ReferenceModel referenceModel) {
//        return getEntityValues().stream()
//                .map(CodegenEntityModel.class::cast)
//                .filter(e -> {
//                    return referenceModel.getReferencedEntityName().equals(e.getId());
//                })
//                .findFirst().orElseThrow(IllegalStateException::new);
//    }
}
