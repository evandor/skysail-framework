package io.skysail.server.db.versions.impl;

import java.util.regex.*;

import lombok.*;

@Getter
public class Migration {

    private Pattern pattern = Pattern.compile("V(\\d+)_(.*)\\.sql");

    private int version;
    private String title;

    /**
     * @param path matching the defined pattern, otherwise exception.
     */
    public Migration(@NonNull String path) {
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
}
