package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class PostTodoWoListResource extends PostTodoResource {

    @Override
    public SkysailResponse<?> addEntity(Todo entity) {
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setStatus(Status.NEW);
        entity.setRank(1);
        entity.setList(listId);
        String id = app.getRepository().add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<String>();
    }

   
}