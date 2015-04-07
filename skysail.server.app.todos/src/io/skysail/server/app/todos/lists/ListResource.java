package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.resources.TodosResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListResource extends EntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;

    public ListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "plus");
    }
    
    @Override
    protected void doInit() {
        listId = getAttribute(TodoApplication.LIST_ID);
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

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }
}
