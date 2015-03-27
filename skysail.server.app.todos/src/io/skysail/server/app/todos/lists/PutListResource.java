package io.skysail.server.app.todos.lists;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;

import java.util.Date;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutListResource extends PutEntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;
    
    public PutListResource() {
        super(TodoApplication.LIST_ID);
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication)getApplication();
    }
    
    @Override
    public SkysailResponse<?> updateEntity(TodoList entity) {
        entity.setModified(new Date());
        app.getRepository().update(listId, entity);
        return new SkysailResponse<>();
    }

    @Override
    public TodoList getEntity() {
        return app.getRepository().getById(TodoList.class, listId);
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}
