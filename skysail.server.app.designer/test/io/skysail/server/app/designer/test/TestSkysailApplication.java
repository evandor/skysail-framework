package io.skysail.server.app.designer.test;

import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.SkysailApplication;

public class TestSkysailApplication extends SkysailApplication {

    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }

}
