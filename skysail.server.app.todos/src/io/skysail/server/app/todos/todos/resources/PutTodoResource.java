package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

import org.restlet.resource.ResourceException;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication) getApplication();
    }

    @Override
    public Todo getEntity() {
        return app.getRepository().getById(Todo.class, getAttribute(TodoApplication.TODO_ID));
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        Todo original = getEntity(null);
        copyProperties(original,entity);
        original.setModified(new Date());
        original.setUrgency(Ranker.calcUrgency(original));
        Integer views = original.getViews();
        if (views == null) {
            original.setViews(1);
        }
        app.getRepository().update(getAttribute(TodoApplication.LIST_ID), original);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
       // return super.redirectTo(TodosResource.class);
        return super.redirectTo(ListsResource.class);
    }

}
