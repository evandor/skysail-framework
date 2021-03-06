package io.skysail.server.app;

import lombok.*;

@EqualsAndHashCode(of = "versionNr") // NO_UCD (unused code)
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

    @Override
    public String toString() {
        return "v" + versionNr;
    }

}
