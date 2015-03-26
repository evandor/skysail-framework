package io.skysail.server.app.todos.lists;

import io.skysail.server.app.todos.TodoApplication;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class ListsResource extends ListServerResource<TodoList> {

    private TodoApplication app;
    
    public ListsResource() {
        super(ListResource.class);
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TodoApplication) getApplication();
    }

    @Override
    public List<TodoList> getEntity() {
        return app.getRepository().findAll(TodoList.class, "");
    }

}
