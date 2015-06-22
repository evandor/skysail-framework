package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class ApplicationModel {

    private final String applicationName;
    
    private final Set<EntityModel> entities = new HashSet<>();

    private final String packageName;

    public ApplicationModel(@NonNull Application application, DesignerRepository repo) {
        this.applicationName = application.getName();
        this.packageName = application.getPackageName();
        setupModel(application,repo);
    }
    
    private void setupModel(Application application, DesignerRepository repo) {
        application.getEntities().stream().forEach(entity -> {
            EntityModel entityModel = addEntity(entity.getName());
            
            List<EntityField> fields = getFields(repo, entity.getName(), application.getId());
            fields.stream().forEach(f -> {
                entityModel.addField(f);
            });
            List<Entity> references = getReferences(repo, entity.getName(), application.getId());
            
            references.stream().forEach(f -> {
                entityModel.addReference(f);
            });
        });
    }


    public EntityModel addEntity(String entityName) {
        log.info("ApplicationModel: adding Entity '{}'", entityName);
        EntityModel entityModel = new EntityModel(entityName);
        if (!entities.add(entityModel)) {
            throw new IllegalStateException("EntityModel '"+entityName+"' already exists!");
        }
        return entityModel;
    }

    public void validate() {
        eachEntitiesReferencesMustPointToExistingEntity();
    }

    private void eachEntitiesReferencesMustPointToExistingEntity() {
        List<String> entityNames = entities.stream().map(EntityModel::getEntityName).collect(Collectors.toList());
        entities.stream().forEach(entity -> {
            entity.getReferences().stream().forEach(reference -> {
                validateReference(reference,entityNames);
            });
        });
    }

    private void validateReference(ReferenceModel reference, List<String> entities) {
        if (!entities.contains(reference.getName())) {
            throw new IllegalStateException("Reference '"+reference.getName()+"' was not contained in the list of known entities: " + entities);
        }
    }
    
    private List<EntityField> getFields(DesignerRepository repo2, String beanName, String appIdentifier) {
        Application designerApplication = repo2.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = designerApplication.getEntities();
        return findFields(repo2, beanName, appIdentifier, entities);
    }

    private List<EntityField> findFields(DesignerRepository repo2, String beanName, String appIdentifier,
            List<Entity> entities) {
        // streams dont't seem to work here ?!?! (with orientdb objects)
        for (Entity entity : entities) {
            if (beanName.equals(entity.getName())) {
                return entity.getFields();
            }
            List<EntityField> fieldsFromSubEntity = findFields(repo2, beanName, appIdentifier, entity.getSubEntities());
            if (fieldsFromSubEntity.size() > 0) {
                return fieldsFromSubEntity;
            }
        }
        return Collections.emptyList();
    }

    private List<Entity> getReferences(DesignerRepository repo2, String beanName, String appIdentifier) {
        Application designerApplication = repo2.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = designerApplication.getEntities();
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



}
