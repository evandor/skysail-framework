package io.skysail.domain.core;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import aQute.bnd.annotation.component.Component;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.domain.core.repos.Repository;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, provide = Repositories.class)
@Slf4j
@ToString
public class Repositories {

    private volatile Map<String, DbRepository> repositories = new ConcurrentHashMap<>(); // NOSONAR

    @aQute.bnd.annotation.component.Reference(dynamic = true, multiple = true, optional = true)
    public void setRepository(@NonNull DbRepository repo) {
        if (repo.getRootEntity() == null) {
            return;
        }
        String identifier = repo.getRootEntity().getName();
        if (identifier == null) {
            throw new IllegalStateException("cannot set repository, name is missing");
        }
        repositories.put(identifier, repo);
        log.debug("(+ Repository)  (#{}) with name '{}'", formatSize(repositories.keySet()),identifier);
    }

    public void unsetRepository(DbRepository repo) {
        String identifier = repo.getRootEntity().getName();
        repositories.remove(identifier);
        log.debug("(- Repository)  name '{}', count is {} now", identifier, formatSize(repositories.keySet()));
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

    private static String formatSize(@NonNull Collection<?> list) {
        return new DecimalFormat("00").format(list.size());
    }

}
