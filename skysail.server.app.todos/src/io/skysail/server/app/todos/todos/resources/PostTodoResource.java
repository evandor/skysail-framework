package io.skysail.server.app.todos.todos.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.services.TodosService;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.utils.ResourceUtils;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    protected TodoApplication app;
    private TodosService todosService;

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (TodoApplication) getApplication();
        getResourceContext().addDisabledAjaxNavigation("Todo-Lists", ListsResource.class);
        todosService = getService(TodosService.class);
    }

    @Override
    public Todo createEntityTemplate() {
        String listId = getAttribute(TodoApplication.LIST_ID);
        if (listId == null) {
            listId = app.getDefaultList(getRequest());
        }
        return new Todo(getQuery(), listId, ResourceUtils.determineLocale(this));
    }

    @Override
    public void addEntity(Todo entity) {
        todosService.addTodo(this, entity);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class);
    }

    @Override
    public String redirectTo() {
        if ("submitAndNew".equals(submitValue)) {
            return super.redirectTo(PostTodoResource.class);
        }
        return super.redirectTo(TodosResource.class);
    }

}