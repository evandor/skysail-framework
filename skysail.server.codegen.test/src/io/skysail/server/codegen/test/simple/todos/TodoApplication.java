package io.skysail.server.codegen.test.simple.todos;

import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.SkysailApplication;
import lombok.Getter;

public class TodoApplication extends SkysailApplication {

    @Getter
    private TodoRepo todoRepository;

    @Override
    public EventAdmin getEventAdmin() {
        return null;
    }
}
