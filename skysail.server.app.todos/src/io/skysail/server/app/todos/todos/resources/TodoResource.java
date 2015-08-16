package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class TodoResource extends EntityServerResource<Todo> {

    private String id;
    private TodoApplication app;
    private String listId;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication) getApplication();
    }

    @Override
    public Todo getEntity() {
        Todo todo = app.getRepository().getById(Todo.class, id);
        todo.setViews(todo.getViews() + 1);
        app.getRepository().update(todo.getId(), todo);
        return todo;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        app.getRepository().delete(Todo.class, id);
        return new SkysailResponse<String>();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutTodoResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }
    
//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> {
//            l.substitute(TodoApplication.LIST_ID, listId);
//        };
//    }

}
