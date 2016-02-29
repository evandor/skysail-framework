package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.services.TodosService;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.*;

import java.util.List;

import org.restlet.resource.ResourceException;

public class TodosResource extends ListServerResource<Todo> {

    protected String listId;
    protected TodoApplication app;

    private TodosService todosService;

    public TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication) getApplication();
//        if (listId != null) {
//            TodoList list = app.getListRepo().getById(listId);
//            Map<String, String> substitutions = new HashMap<>();
//            substitutions.put("/Lists/" + listId, list.getName());
//            getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
//        }
        getResourceContext().addAjaxNavigation("ajax", "Todo-Lists", ListsResource.class, TodosResource.class, "id");
        todosService = getService(TodosService.class);
    }

    @Override
    public List<Todo> getEntity() {
        return todosService.getTodos(this, getAttribute(TodoApplication.LIST_ID));
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        links.add(PostTodoResource.class);
        links.add(ArchivedTodosResource.class);
        return super.getLinks(links);
    }


}
