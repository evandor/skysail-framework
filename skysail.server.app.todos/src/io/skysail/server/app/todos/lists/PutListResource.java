package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.app.todos.todos.resources.Top10TodosResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PutListResource extends PutEntityServerResource<TodoList> {

    private ListService listService;

    public PutListResource() {
        super(TodoApplication.LIST_ID);
        addToContext(ResourceContextId.LINK_TITLE, "edit list");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        listService = getService(ListService.class);
    }

    @Override
    public SkysailResponse<TodoList> updateEntity(TodoList entity) {
        return listService.updateList(this, entity);
    }

    @Override
    public TodoList getEntity() {
        return listService.getList(this,getAttribute(TodoApplication.LIST_ID));
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
