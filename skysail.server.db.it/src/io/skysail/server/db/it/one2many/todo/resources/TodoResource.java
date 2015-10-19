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
        Todo todo = app.getRepository().findOne(getAttribute("id"));
        todo.getComments().forEach(comment -> app.getRepository().deleteVertex(comment.getId()));
        app.getRepository().deleteVertex(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public Todo getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

}
