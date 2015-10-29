package io.skysail.server.app;

import io.skysail.api.repos.DbRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Component;

@Component(immediate = true, provide = Repositories.class)
@Slf4j
public class Repositories {

    private volatile Map<String, DbRepository> repositories = new ConcurrentHashMap<>();

    @aQute.bnd.annotation.component.Reference(dynamic = true, multiple = true, optional = true)
    public void setRepository(DbRepository repo) {
        String identifier = repo.getRootEntity().getName();
        log.info("adding repository with name '{}'", identifier);
        repositories.put(identifier, repo);
    }

    public void unsetRepository(DbRepository repo) {
        String identifier = repo.getRootEntity().getName();
        log.info("removing repository with name '{}'", identifier);
        repositories.remove(identifier);
    }

}
