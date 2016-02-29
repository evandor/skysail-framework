package io.skysail.server.app.todos.lists;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.app.todos.todos.resources.Top10TodosResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostListResource extends PostEntityServerResource<TodoList> {

    private ListService listService;

    public PostListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new List");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        getResourceContext().addDisabledAjaxNavigation("Todo-Lists", ListsResource.class);
        listService = getService(ListService.class);
    }

    @Override
    public TodoList createEntityTemplate() {
        return new TodoList();
    }

    @Override
    public void addEntity(TodoList entity) {
        listService.addList(this,entity);
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
