package io.skysail.server.db.it.one2many.todo.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private TodoApplication app;

    protected void doInit() {
        super.doInit();
        app = (TodoApplication) getApplication();
    }

    public Todo getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

    public SkysailResponse<Todo> updateEntity(Todo entity) {
        Todo original = getEntity(null);
        original.setTitle(entity.getTitle());
        original.setModified(new Date());
        Object update = app.getRepository().update(getAttribute("id"), original);
        return new SkysailResponse<>();
    }

}
