package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.domain.resources.TodosResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class ListResource extends EntityServerResource<TodoList> {

    private String id;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication)getApplication();
    }
    
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TodoList getEntity() {
        return app.getRepository().getById(TodoList.class, id);
    }
    
    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ListResource.class,TodosResource.class,PutListResource.class);
    }

}
