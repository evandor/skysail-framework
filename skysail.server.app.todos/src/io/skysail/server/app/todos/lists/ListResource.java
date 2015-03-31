package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.resources.TodosResource;

import java.util.List;

import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class ListResource extends EntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        String attribute = getAttribute(TodoApplication.LIST_ID);
        if (attribute != null) { // TODO create getAttribute and decode right away in superclass
            listId = Reference.decode(attribute);
        }
        app = (TodoApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO cascade todos (?)
        app.getRepository().delete(TodoList.class, listId);
        return null;
    }

    @Override
    public TodoList getEntity() {
        return app.getRepository().getById(TodoList.class, listId);
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(ListResource.class, TodosResource.class, PutListResource.class);
    }

}
