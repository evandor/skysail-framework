package io.skysail.server.app.designer.test;

import io.skysail.server.app.designer.application.Application;
import lombok.Builder;

@Builder
public class TestApplication {

    private String name;

    public Application create() {
        return null;
    }

}
