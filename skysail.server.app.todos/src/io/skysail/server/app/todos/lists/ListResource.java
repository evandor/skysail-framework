package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.domain.resources.TodosResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class ListResource extends EntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication)getApplication();
    }
    
    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO cascade todos (?)
        app.getRepository().delete(listId);
        return null;
    }

    @Override
    public TodoList getEntity() {
        return app.getRepository().getById(TodoList.class, listId);
    }
    
    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ListResource.class,TodosResource.class,PutListResource.class);
    }

}
