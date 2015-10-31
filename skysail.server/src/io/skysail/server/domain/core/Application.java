package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.Repository;
import io.skysail.server.app.Repositories;

import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class Application implements Identifiable {

    @Getter
    @Setter
    private String id;

    private List<Entity> entities = new ArrayList<>();

    private Repositories repositories;

    public Application(String id) {
        this.id = id;
    }

    public Application add(@NonNull Entity entity) {
        entities.add(entity);
        return this;
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
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
