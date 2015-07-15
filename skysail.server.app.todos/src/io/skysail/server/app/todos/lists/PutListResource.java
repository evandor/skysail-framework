package io.skysail.server.app.todos.lists;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PutListResource extends PutEntityServerResource<TodoList> {

    private String listId;
    private TodoApplication app;

    public PutListResource() {
        super(TodoApplication.LIST_ID);
        addToContext(ResourceContextId.LINK_TITLE, "edit list");
    }

    @Override
    protected void doInit() throws ResourceException {
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(TodoList entity) {
        TodoList original = getEntity(null);
        original.setName(entity.getName());
        original.setDesc(entity.getDesc());
        original.setModified(new Date());
        app.getRepository().update(listId, original);
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
