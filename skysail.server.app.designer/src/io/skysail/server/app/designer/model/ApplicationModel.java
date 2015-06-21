package io.skysail.server.app.designer.model;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class ApplicationModel {

    private String applicationName;
    
    private Set<EntityModel> entities = new HashSet<>();

    public ApplicationModel(String applicationName) {
        this.applicationName = applicationName;
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

}
