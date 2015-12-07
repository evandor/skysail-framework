package io.skysail.server.app.designer.test;

import java.util.Arrays;

import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import io.skysail.server.app.designer.application.Application;

public class TestApplication extends SkysailApplication {

    public TestApplication() {
        super("testapplication", new ApiVersion(1), Arrays.asList());
    }

    public Application create() {
        return null;
    }

    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }

}
