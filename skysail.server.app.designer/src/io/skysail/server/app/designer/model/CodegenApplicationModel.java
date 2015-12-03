package io.skysail.server.app.designer.model;

import java.util.*;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.domain.core.ApplicationModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class CodegenApplicationModel extends ApplicationModel {

    private final String applicationName;
    //private final Set<CodegenEntityModel> entityModels = new HashSet<>();
    private final String packageName;

    private String path;
    private String projectName;

    public CodegenApplicationModel(Application application, DesignerRepository repo) {
        super(application.getName());
        this.applicationName = application.getName();
        this.packageName = application.getPackageName();
        path = application.getPath();
        projectName = application.getProjectName();
        setupModel(application, repo);
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

//    @SuppressWarnings("unused")
//    private CodegenEntityModel getEntityModel(Entity entity) {
//        return getEntities().stream()
//                .map(CodegenEntityModel.class::cast)
//                .filter(em -> {
//                    return em.getId().equals(entity.getName());
//                })
//                .findFirst().orElseThrow(IllegalStateException::new);
//    }

    private void createEntityModel(Application application, DesignerRepository repo, Entity entity) {
        CodegenEntityModel entityModel = addEntity(entity);

        List<EntityField> fields = getFields(repo, entity.getName(), application.getId());
        fields.stream().forEach(f -> {
            entityModel.addField(f);
        });

        List<ActionEntityField> actionFields = getActionFields(repo, entity.getName(), application.getId());
        actionFields.stream().forEach(f -> {
            entityModel.addActionField(f);
        });

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

//    private void eachEntitiesReferencesMustPointToExistingEntity() {
//        List<String> entityNames = getEntities().stream()
//                .map(CodegenEntityModel.class::cast)
//                .map(CodegenEntityModel::getId)
//                .collect(Collectors.toList());
//        getEntities().stream().forEach(entity -> {
//            entity.getReferences().stream().forEach(reference -> {
//                validateReference(reference, entityNames);
//            });
//        });
//    }

    private void validateReference(ReferenceModel reference, List<String> entities) {
        if (!entities.contains(reference.getReferencedEntityName())) {
            throw new IllegalStateException("Reference '" + reference.getReferencedEntityName()
                    + "' was not contained in the list of known entities: " + entities);
        }
    }

    private List<EntityField> getFields(DesignerRepository repo2, String beanName, String appIdentifier) {
        Application designerApplication = repo2.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = designerApplication.getEntities();
        return findFields(repo2, beanName, appIdentifier, entities);
    }

    private List<ActionEntityField> getActionFields(DesignerRepository repo2, String beanName, String appIdentifier) {
        Application designerApplication = repo2.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = designerApplication.getEntities();
        return findActionFields(repo2, beanName, appIdentifier, entities);
    }

    private List<EntityField> findFields(DesignerRepository repo2, String beanName, String appIdentifier,
            List<Entity> entities) {
        for (Entity entity : entities) {
            if (beanName.equals(entity.getName())) {
                return entity.getFields();
            }
            List<EntityField> fieldsFromSubEntity = findFields(repo2, beanName, appIdentifier, entity.getSubEntities());
            if (!fieldsFromSubEntity.isEmpty()) {
                return fieldsFromSubEntity;
            }
        }
        return Collections.emptyList();
    }

    private List<ActionEntityField> findActionFields(DesignerRepository repo2, String beanName, String appIdentifier,
            List<Entity> entities) {
        // streams dont't seem to work here ?!?! (with orientdb objects)
        for (Entity entity : entities) {
            if (beanName.equals(entity.getName())) {
                return entity.getActionFields();
            }
        }
        return Collections.emptyList();
    }

    private List<Entity> getReferences(DesignerRepository repo2, String beanName, String appIdentifier) {
        Application designerApplication = repo2.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = null;//designerApplication.getEntities();
        return findReferences(repo2, beanName, appIdentifier, entities);
    }

    private List<Entity> findReferences(DesignerRepository repo2, String beanName, String appIdentifier,
            List<Entity> entities) {
        // streams dont't seem to work here ?!?! (with orientdb objects)
        for (Entity entity : entities) {
            if (beanName.equals(entity.getName())) {
                return entity.getSubEntities();
            }
            List<Entity> fieldsFromSubEntity = findReferences(repo2, beanName, appIdentifier, entity.getSubEntities());
            if (fieldsFromSubEntity.size() > 0) {
                return fieldsFromSubEntity;
            }
        }
        return Collections.emptyList();
    }

    public CodegenEntityModel getEntityModel(ReferenceModel referenceModel) {
        return getEntityValues().stream()
                .map(CodegenEntityModel.class::cast)
                .filter(e -> {
                    return referenceModel.getReferencedEntityName().equals(e.getId());
                })
                .findFirst().orElseThrow(IllegalStateException::new);
    }
}
