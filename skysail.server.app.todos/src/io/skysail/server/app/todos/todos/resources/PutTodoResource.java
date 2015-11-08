package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.services.TodosService;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.*;

import java.util.List;

import org.restlet.resource.ResourceException;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private TodoApplication app;
    private TodosService todosService;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication) getApplication();
        todosService = getService(TodosService.class);
    }

    @Override
    public Todo getEntity() {
        return todosService.getTodo(this, getAttribute(TodoApplication.TODO_ID));
    }

    @Override
    public SkysailResponse<Todo> updateEntity(Todo entityFromTheWire) {
        return todosService.update(this, entityFromTheWire);
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        return super.getLinks(links);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}
