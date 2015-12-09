package io.skysail.server.app.designer.test;

import io.skysail.server.app.designer.application.DbApplication;
import lombok.Builder;

@Builder
public class TestApplication {

    private String name;

    public DbApplication create() {
        return null;
    }

}
