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
    private final String id;

    /** the applications entities in a map with their id as key. */
    private Map<String, EntityModel> entities = new HashMap<>();

    /** the applications aggregate repositories. */
    private Repositories repositories = new Repositories();

    /** an applications unique id, e.g. a full qualified java identifier. */
    public ApplicationModel(String id) {
        this.id = id;
    }

    /**
     * adds an non-null entity model identified by its id. If an entity model with
     * the same id exists already, a warning is issued.
     */
    public ApplicationModel add(@NonNull EntityModel entity) {
        if (entities.get(entity.getId()) != null) {
            log.warn("entity {} already exists - not adding to application {}", entity.getId(), this.getId());
            return this;
        }
        entities.put(entity.getId(), entity);
        return this;
    }

    /**
     * @return all entities ids.
     */
    public Set<String> getEntityIds() {
        return entities.keySet();
    }

    /**
     * returns the entity model for the given entity id, if existent.
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

    public void setRepositories(Repositories repos) {
        this.repositories = repos;
    }

}
