package io.skysail.server.app;

import io.skysail.api.repos.DbRepository;

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
            throw new IllegalStateException("cannot set repository");
        }
        String identifier = repo.getRootEntity().getName();
        if (identifier == null) {
            throw new IllegalStateException("cannot set repository, name is missing");
        }
        log.info("adding repository with name '{}'", identifier);
        DbRepository put = repositories.put(identifier, repo);
        System.out.println(put);
    }

    public void unsetRepository(DbRepository repo) {
        String identifier = repo.getRootEntity().getName();
        log.info("removing repository with name '{}'", identifier);
        repositories.remove(identifier);
    }

    public synchronized Map<String, DbRepository> getRepositories() {
        return Collections.unmodifiableMap(repositories);
    }

}
