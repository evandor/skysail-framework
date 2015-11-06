package io.skysail.server.db.versions.impl;

import java.util.List;
import java.util.regex.*;

import lombok.*;

@Getter
@ToString(of = {"version", "title", "migrationType"})
public class Migration implements Comparable<Migration> {

    private enum MIG_TYPE {
        SQL, CMD
    }

    private Pattern pattern = Pattern.compile("V(\\d+)_(.*)[\\.sql|\\.cmd]");

    private Integer version;
    private String title;
    private String content;

    private MIG_TYPE migrationType = MIG_TYPE.SQL;

    /**
     * @param path
     *            matching the defined pattern, otherwise exception.
     * @param content
     */
    public Migration(@NonNull String path, String content) {
        this.content = content;
        validate(path);
        if (path.toLowerCase().endsWith(".cmd")) {
            migrationType = MIG_TYPE.CMD;
        }
    }

    private void validate(String path) {
        Matcher matcher = pattern.matcher(path);
        boolean found = false;
        while (matcher.find()) {
            version = Integer.parseInt(matcher.group(1));
            title = matcher.group(2);
            found = true;
        }
        if (!found) {
            throw new IllegalArgumentException(
                    "filename must start with V, followed by a number, optionally followed with '_sometext', with a postfix of '.sql|.cmd'");
        }
    }

    @Override
    public int compareTo(Migration o) {
        return version.compareTo(o.getVersion());
    }

    public void runMigration(VersionsRepository repo, List<String> results) {
        switch (migrationType) {
        case CMD:
            runCommand(repo, results);
            break;
        default:
            runSqlMigration(repo, results);
            break;
        }
    }

    private void runCommand(VersionsRepository repo, List<String> results) {
        String sql = getContent().trim().replace("\n", " ").replace("\r", " ").trim();
        if (sql.length() > 5) {
            Object result = repo.execute(getContent().trim());
            results.add("run '" + sql + "' with result: " + result.toString());
        }
    }

    private void runSqlMigration(VersionsRepository repo, List<String> results) {
        String[] statements = getContent().split(";");
        for (String statement : statements) {
            String sql = statement.trim().replace("\n", " ").replace("\r", " ").trim();
            if (sql.length() > 5) {
                Object result = repo.execute(statement.trim());
                results.add("run '" + sql + "' with result: " + result.toString());
            }
        }
    }
}
