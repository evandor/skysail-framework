package io.skysail.server.app.todos.lists;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutListResource extends PutEntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication)getApplication();
    }
    
    @Override
    public SkysailResponse<?> updateEntity(TodoList entity) {
        //app.getRepository().update(entity);
        return new SkysailResponse<>();
    }

    @Override
    public TodoList getEntity() {
        return app.getRepository().getById(TodoList.class, listId);
    }

}
