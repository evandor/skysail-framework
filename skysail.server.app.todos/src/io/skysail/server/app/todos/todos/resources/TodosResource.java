package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.*;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodosResource extends ListServerResource<Todo> {

    public static final String DEFAULT_FILTER_EXPRESSION = "(!(status=" + Status.DONE + "))";

    protected String listId;
    protected TodoApplication app;

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
        if (listId != null) {
            TodoList list = app.getListRepo().getById(listId);
            Map<String, String> substitutions = new HashMap<>();
            substitutions.put("/Lists/" + listId, list.getName());
            getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
        }
        getResourceContext().addAjaxNavigation("ajax", "Todo-Lists", ListsResource.class, TodosResource.class, "id");
    }

    @Override
    public List<Todo> getEntity() {
        Filter filter = new Filter(getRequest(), DEFAULT_FILTER_EXPRESSION);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        filter.addEdgeOut("parent", "#" + listId);

        Pagination pagination = new Pagination(getRequest(), getResponse(), app.getTodosRepo().getTodosCount(listId,
                filter));
        return app.getTodosRepo().findAllTodos(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        List<Class<? extends SkysailServerResource<?>>> links = app.getMainLinks();
        links.add(PostTodoResource.class);
        links.add(ArchivedTodosResource.class);
        return super.getLinks(links);
    }


}
