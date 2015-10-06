package io.skysail.server.db.versions;

import io.skysail.server.db.versions.impl.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.repo.DbRepository;
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
            maxVersion = -1;
        } else {
            maxVersion = found.stream().map(c -> c.getVersion()).reduce(0, (a,b) -> Math.max(a,b)).intValue();
        }

        List<String> results = new ArrayList<>();
        try {
            Enumeration<String> entryPathsEnumeration = bundle.getEntryPaths(DB_MIGRATIONS_PATH);
            if (entryPathsEnumeration == null) {
                return;
            }
            List<String> entryPaths = Collections.list(entryPathsEnumeration);
            List<Migration> migrations = entryPaths.stream()
                .map(path -> path.substring(DB_MIGRATIONS_PATH.length()))
                .filter(path -> path.startsWith("V"))
                .filter(path -> path.endsWith(".sql"))
                .map(path -> new Migration(path, BundleUtils.readResource(bundle, DB_MIGRATIONS_PATH + "/" + path)))
                .collect(Collectors.toList());

            validate(migrations);
            Collections.sort(migrations);
            execute(bundle, migrations, maxVersion, results);
            results.stream().forEach(r -> log.info(r));
        } catch (Exception e) {
            log.warn("could not run migrations: {}", e.getMessage());
            results.stream().forEach(r -> log.error(r));
        }
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
            String[] statements = m.getContent().split(";");
            for (String statement : statements) {
                String sql = statement.trim().replace("\n", " ").replace("\r", " ").trim();
                if (sql.length() > 5) {
                    Object result = repo.execute(statement.trim());
                    results.add("run '" + sql + "' with result: " + result.toString());
                }
            }
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
