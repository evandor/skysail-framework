package io.skysail.server.db.versions;

import io.skysail.server.db.versions.impl.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.repo.DbRepository;

import java.util.*;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, provide = VersioningService.class)
public class VersioningService {

    private VersionsRepository repo;

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=VersionsRepository)")
    public void setTodoRepository(DbRepository repo) {
        this.repo = (VersionsRepository) repo;
    }

    public void unsetTodoRepository(DbRepository repo) {
        this.repo = null;
    }

    public void register(Bundle bundle) {
        Filter filter = new Filter();
        filter.add("entity", bundle.getSymbolicName());
        List<ComponentDbVersion> found = repo.find(filter);
        Integer maxVersion;
        if (found.size() == 0) {
            //repo.save(new ComponentDbVersion(bundle));
            maxVersion = -1;
        } else {
            maxVersion = found.stream().map(c -> c.getVersion()).reduce(0, (a,b) -> Math.max(a,b)).intValue();
        }

        Enumeration<String> entryPaths = bundle.getEntryPaths("db/migrations");
        List<Migration> migrations = Collections.list(entryPaths).stream()
            .filter(path -> path.startsWith("V"))
            .filter(path -> path.endsWith(".sql"))
            .map(path -> new Migration(path))
            .collect(Collectors.toList());

        validate(migrations);
    }

    private void validate(List<Migration> migrations) {
        List<Integer> versionNumbers = migrations.stream().map(Migration::getVersion).collect(Collectors.toList());
        Set<Integer> set = new HashSet<>();
        versionNumbers.forEach(v -> {
            if (!set.add(v)) {
                throw new IllegalStateException("at least two migrations contained the same version number: " + v);
            }
        });
    }
}
