package io.skysail.server.app.designer.test;

import java.util.Arrays;

import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;

public class TestSkysailApplicationGen extends SkysailApplication {
    
    public TestSkysailApplicationGen() {
        super("testapplication", new ApiVersion(1), Arrays.asList());
    }

    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }

}
