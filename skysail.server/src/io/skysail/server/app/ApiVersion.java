package io.skysail.server.app;

import lombok.NonNull;

public class ApiVersion {

    private Integer versionNr;

    public ApiVersion(@NonNull Integer versionNr) {
        if (versionNr <= 0) {
            throw new IllegalArgumentException("the version number must be greater that 0");
        }
        this.versionNr = versionNr;
    }

    public String getVersionPath() {
        return new StringBuilder("/v").append(versionNr).toString();
    }

}
