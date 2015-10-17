package io.skysail.server.db.it.one2many.todo.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TodoResource extends EntityServerResource<Todo> {

    private TodoApplication app;

    protected void doInit() {
        app = (TodoApplication) getApplication();
    }
    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Todo getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

}
