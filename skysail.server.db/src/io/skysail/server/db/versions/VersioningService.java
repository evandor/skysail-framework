package io.skysail.server.db.versions;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.versions.impl.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.utils.BundleUtils;

import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, provide = VersioningService.class)
@Slf4j
public class VersioningService {

    private static final String DB_MIGRATIONS_PATH = "/db/migrations";

    private VersionsRepository repo;

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=VersionsRepository)")
    public void setVersionsRepository(DbRepository repo) {
        this.repo = (VersionsRepository) repo;
    }

    public void unsetVersionsRepository(DbRepository repo) {
        this.repo = null;
    }

    public void register(Bundle bundle) {
        List<String> results = new ArrayList<>();
        try {
            List<ComponentDbVersion> entityVersions = repo.find(new Filter().add("entity", bundle.getSymbolicName()));
            migrateBundleDb(bundle, getCurrentVersionNr(entityVersions), results);
        } catch (Exception e) {
            log.warn("could not run migrations: {}", e.getMessage());
            results.stream().forEach(r -> log.error(r));
        }
    }

    private void migrateBundleDb(Bundle bundle, Integer currentVersion, List<String> results) {
        Enumeration<String> entryPathsEnumeration = bundle.getEntryPaths(DB_MIGRATIONS_PATH);
        if (entryPathsEnumeration == null) {
            return;
        }
        List<String> entryPaths = Collections.list(entryPathsEnumeration);
        List<Migration> migrations = entryPaths.stream()
            .map(path -> path.substring(DB_MIGRATIONS_PATH.length()))
            .filter(path -> path.startsWith("V"))
            .filter(path -> path.endsWith(".sql") || path.endsWith(".cmd"))
            .map(path -> new Migration(path, BundleUtils.readResource(bundle, DB_MIGRATIONS_PATH + "/" + path)))
            .collect(Collectors.toList());

        validate(migrations);
        Collections.sort(migrations);
        execute(bundle, migrations, currentVersion, results);
        results.stream().forEach(r -> log.info(r));
    }

    private Integer getCurrentVersionNr(List<ComponentDbVersion> entityVersions) {
        Integer maxVersion;
        if (entityVersions.size() == 0) {
            maxVersion = -1;
        } else {
            maxVersion = entityVersions.stream().map(c -> c.getVersion()).reduce(0, (a,b) -> Math.max(a,b)).intValue();
        }
        return maxVersion;
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

    private void execute(Bundle bundle, List<Migration> migrations, Integer currentVersion, List<String> results) {
        migrations.stream()
            .filter(m -> m.getVersion() > currentVersion)
            .forEach(m -> results.addAll(runSql(bundle, m)));
    }

    private List<String> runSql(Bundle bundle, Migration m) {
        ComponentDbVersion componentVersion = new ComponentDbVersion(bundle, m.getTitle(), m.getVersion());
        String migrationId = repo.save(componentVersion).toString();
        componentVersion.setId(migrationId);

        List<String> results = new ArrayList<>();

        try {
            m.runMigration(repo, results);
            componentVersion.setStatus(Status.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            results.add(e.getMessage());
            componentVersion.setStatus(Status.FAILURE);
        }

        repo.update(migrationId, componentVersion);
        return results;
    }

}
