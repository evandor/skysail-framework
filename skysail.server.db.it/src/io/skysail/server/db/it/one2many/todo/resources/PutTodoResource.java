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

    public SkysailResponse<Todo> updateEntity(Todo entityFromTheWire) {
        Todo entityToBeUpdated = getEntity(null);
        copyProperties(entityToBeUpdated, entityFromTheWire);
        entityToBeUpdated.setModified(new Date());
        app.getRepository().update(getAttribute("id"), entityToBeUpdated, "comments");
        return new SkysailResponse<>(entityToBeUpdated);
    }

}