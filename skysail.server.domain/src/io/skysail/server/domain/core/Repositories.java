package io.skysail.server.domain.core;

import io.skysail.api.repos.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Component;

@Component(immediate = true, provide = Repositories.class)
@Slf4j
public class Repositories {

    private volatile Map<String, DbRepository> repositories = new ConcurrentHashMap<>();

    @aQute.bnd.annotation.component.Reference(dynamic = true, multiple = true, optional = true)
    public void setRepository(@NonNull DbRepository repo) {
        if (repo.getRootEntity() == null) {
            throw new IllegalStateException("cannot set repository '" + repo.getClass().getName()
                    + "' as it does not have a root entity");
        }
        String identifier = repo.getRootEntity().getName();
        if (identifier == null) {
            throw new IllegalStateException("cannot set repository, name is missing");
        }
        repositories.put(identifier, repo);
        log.info("(+ Repository)  (#{}) with name '{}'", repositories.size(),identifier);
    }

    public void unsetRepository(DbRepository repo) {
        String identifier = repo.getRootEntity().getName();
        repositories.remove(identifier);
        log.info("(- Repository)  name '{}', count is {} now", identifier, repositories.size());
    }

    public synchronized Map<String, DbRepository> getRepositories() {
        return Collections.unmodifiableMap(repositories);
    }

    public Repository get(String identifier) {
        return repositories.get(identifier);
    }

    public Collection<String> getRepositoryIdentifiers() {
        return repositories.keySet();
    }

}
