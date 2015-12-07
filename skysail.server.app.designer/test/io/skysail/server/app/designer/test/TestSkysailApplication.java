package io.skysail.server.app.designer.test;

import java.util.Arrays;

import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;

public class TestSkysailApplication extends SkysailApplication {
    
    public TestSkysailApplication() {
        super("testapplication", new ApiVersion(1), Arrays.asList());
    }

    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }

}
