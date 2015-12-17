package io.skysail.server.domain.core;

import java.util.*;

import io.skysail.api.repos.Repository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the root class of skysail's core domain, describing an application, which aggregates
 * entities and keeps track of repositories to persist those entities.
 *
 * According to specific needs, the core domain can be adapted by extending the corresponding
 * classes. For example, there's a domain extension dealing with the creation of java source
 * files and classes according to a specific core domain model.
 *
 */
@ToString
@Slf4j
public class ApplicationModel {

    @Getter
    /** an identifier, could be a full qualified class name. */
    private final String name;

    /** the applications entities in a map with their name as key. */
    private Map<String, EntityModel> entities = new HashMap<>();

    /** the applications aggregate repositories. */
    @Setter
    private Repositories repositories = new Repositories();

    /** an applications unique name; could be a full qualified java identifier. */
    public ApplicationModel(String fullQualifiedClassName) {
        this.name = fullQualifiedClassName;
    }

    /**
     * adds an non-null entity model identified by its name. If an entity model with
     * the same name exists already, a debug message is issued and the entity model will
     * not be added again.
     */
    public ApplicationModel addOnce(@NonNull EntityModel entityModel) {
        if (entities.get(entityModel.getId()) != null) {
            log.debug("entity {} already exists - not adding to application {}", entityModel.getId(), this.getName());
            return this;
        }
        log.info("adding entity model with id '{}': {}", entityModel.getId(), entityModel );
        entities.put(entityModel.getId(), entityModel);
        return this;
    }

    /**
     * @return all entities ids.
     */
    public Set<String> getEntityIds() {
        return entities.keySet();
    }

    /**
     * returns the entity model for the given entity name, if existent.
     */
    public EntityModel getEntity(String entityId) {
        return entities.get(entityId);
    }

    public Collection<EntityModel> getEntityValues() {
        return entities.values();
    }

    public Repository getRepository(String repositoryId) {
        return repositories.get(repositoryId);
    }

    public Collection<String> getRepositoryIds() {
        return repositories.getRepositoryIdentifiers();
    }

}
