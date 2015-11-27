package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.Repository;

import java.util.*;

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
public class Application implements Identifiable {

    @Getter
    @Setter
    private String id;

    private Map<String, Entity> entities = new HashMap<>();

    private Repositories repositories = new Repositories();

    public Application(String id) {
        this.id = id;
    }

    public Application add(@NonNull Entity entity) {
        if (entities.get(entity.getId()) != null) {
            log.warn("entity {} already exists - not adding to application {}", entity.getId(), this.getId());
            return this;
        }
        entities.put(entity.getId(), entity);
        return this;
    }

    public Set<String> getEntityNames() {
        return entities.keySet();
    }

    public Entity getEntity(String identifier) {
        return entities.get(identifier);
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public Repository getRepository(String identifier) {
        return repositories.get(identifier);
    }

    public Collection<String> getRepositoryIdentifiers() {
        return repositories.getRepositoryIdentifiers();
    }

    public void setRepositories(Repositories repos) {
        this.repositories = repos;
    }

}
