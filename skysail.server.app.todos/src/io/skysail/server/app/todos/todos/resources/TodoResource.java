package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;
import java.util.function.Consumer;

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
        return app.getRepository().getById(Todo.class, id);
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
    public List<Link> getLinkheader() {
        return super.getLinkheader(PutTodoResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }
    
    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            l.substitute(TodoApplication.LIST_ID, listId);
        };
    }

}
