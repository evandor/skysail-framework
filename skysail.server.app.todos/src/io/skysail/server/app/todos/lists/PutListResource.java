package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.resources.Top10TodosResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.*;

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
        super.doInit();
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(TodoList entity) {
        if (entity.isDefaultList()) {
            app.clearUsersOtherLists(getRequest());
        }

        TodoList original = getEntity(null);
        original.setName(entity.getName());
        original.setDesc(entity.getDesc());
        original.setDefaultList(entity.isDefaultList());
        original.setModified(new Date());
        app.getRepository().update(listId, original);
        return new SkysailResponse<>();
    }

    @Override
    public TodoList getEntity() {
        return app.getRepository().getById(TodoList.class, listId);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}
