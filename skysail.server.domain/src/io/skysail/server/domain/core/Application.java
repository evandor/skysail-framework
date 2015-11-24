package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.Repository;

import java.util.*;

import lombok.*;

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
public class Application implements Identifiable {

    @Getter
    @Setter
    private String id;

    private Map<String, Entity> entities = new HashMap<>();

    private Repositories repositories;

    public Application(String id) {
        this.id = id;
    }

    public Application add(@NonNull Entity entity) {
        entities.put(entity.getId(), entity);
        return this;
    }

    public Set<String> getEntityNames() {
        return entities.keySet();
    }

    public Entity getEntity(String identifier) {
        return entities.get(identifier);
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
