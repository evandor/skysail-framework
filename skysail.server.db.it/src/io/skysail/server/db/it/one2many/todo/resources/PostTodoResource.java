package io.skysail.server.db.it.one2many.todo.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.Date;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    @Override
    public Todo createEntityTemplate() {
        return new Todo();
    }

    @Override
    public SkysailResponse<Todo> addEntity(Todo entity) {
        entity.setCreated(new Date());
        String id = ((TodoApplication)getApplication()).getRepository().save(entity, "comments").toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);    }
}
