package io.skysail.server.app.designer.codegen.templates;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class StPath {

    String bundleDir, templateName;
    
    public StPath(@NonNull String path) {
        String trimmedPath = path.trim();
        if (StringUtils.isEmpty(trimmedPath)) {
            throw new IllegalArgumentException("provided path was empty");
        }
        String fullPath = ("/code/" + path).replace("//", "/");
        int lastIndexOfSlash = fullPath.lastIndexOf("/");
        bundleDir = fullPath.substring(0, lastIndexOfSlash);
        templateName = fullPath.substring(lastIndexOfSlash + 1).replace(".stg", "").replace(".st", "");
    }

}
