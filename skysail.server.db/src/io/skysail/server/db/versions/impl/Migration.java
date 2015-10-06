package io.skysail.server.db.versions.impl;

import java.util.regex.*;

import lombok.*;

@Getter
public class Migration implements Comparable<Migration> {

    private Pattern pattern = Pattern.compile("V(\\d+)_(.*)\\.sql");

    private Integer version;
    private String title;
    private String content;

    /**
     * @param path matching the defined pattern, otherwise exception.
     * @param content
     */
    public Migration(@NonNull String path, String content) {
        this.content = content;
        validate(path);
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
                    "filename must start with V, followed by a number, optionally followed with '_sometext', with a postfix of '.sql'");
        }
    }

    @Override
    public int compareTo(Migration o) {
        return version.compareTo(o.getVersion());
    }
}
