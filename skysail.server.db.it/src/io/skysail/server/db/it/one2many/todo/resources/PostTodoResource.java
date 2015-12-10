package io.skysail.server.db.it.one2many.todo.resources;

import java.util.Date;

import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    @Override
    public Todo createEntityTemplate() {
        return new Todo();
    }

    @Override
    public void addEntity(Todo entity) {
        entity.setCreated(new Date());
        String id = ((TodoApplication) getApplication()).getRepository().save(entity, "comments").getId().toString();
        entity.setId(id);
    }
}
