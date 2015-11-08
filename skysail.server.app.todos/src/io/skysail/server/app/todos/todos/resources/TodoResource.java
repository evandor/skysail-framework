package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.services.TodosService;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class TodoResource extends EntityServerResource<Todo> {

    private String id;
    private TodoApplication app;
    private TodosService todosService;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (TodoApplication) getApplication();
        todosService = getService(TodosService.class);
    }

    @Override
    public Todo getEntity() {
        return todosService.getTodo(this, getAttribute("id"));
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return todosService.delete(this, id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutTodoResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }

}
