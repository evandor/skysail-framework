package io.skysail.server.codegen.test.simple;

import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.SkysailApplication;

public class TodoApplication extends SkysailApplication {

    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }

//    @Getter
//    private TodoRepo todoRepository;
}
