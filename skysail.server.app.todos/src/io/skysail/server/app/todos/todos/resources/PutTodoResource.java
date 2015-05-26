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

    private String listId;
    private String todoId;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        listId = getAttribute(TodoApplication.LIST_ID);
        todoId = getAttribute(TodoApplication.TODO_ID);
        app = (TodoApplication) getApplication();
    }

    @Override
    public Todo getEntity() {
        return app.getRepository().getById(Todo.class, todoId);
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        Todo original = getEntity();
        copyProperties(original,entity);
        original.setModified(new Date());
        original.setUrgency(Ranker.calcUrgency(original));
        app.getRepository().update(listId, original);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
       // return super.redirectTo(TodosResource.class);
        return super.redirectTo(ListsResource.class);
    }

}
