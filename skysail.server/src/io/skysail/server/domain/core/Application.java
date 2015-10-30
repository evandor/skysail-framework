package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.*;

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
    private Map<String, Repository> repositories = new HashMap<>();

    public Application(String id) {
        this.id = id;
    }

    public Application add(@NonNull Entity entity) {
        entities.add(entity);
        return this;
    }

//    public Application add(@NonNull Repository repository) {
//        String identifier = repository.getRootEntity().getName();
//        log.info("adding repository {} with identifier {}", repository, identifier);
//        repositories.put(identifier, repository);
//        return this;
//    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public Repository getRepository(String identifier) {
        return repositories.get(identifier);
    }

    public void addRepository(String key, DbRepository value) {
        log.info("adding repository '{}' -> {}", key, value);
        repositories.put(key, value);
    }

    public Collection<String> getRepositoryIdentifiers() {
        return repositories.keySet();
    }

}
