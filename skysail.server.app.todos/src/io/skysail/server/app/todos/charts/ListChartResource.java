package io.skysail.server.app.todos.charts;

import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.todos.resources.TodosResource;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListChartResource extends ListServerResource<TodoChart> {

    public static final String DEFAULT_FILTER_EXPRESSION = "(!(status=" + Status.ARCHIVED + "))";

    protected String listId;
    protected TodoApplication app;

    public ListChartResource() {
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication) getApplication();
        if (listId != null) {
            TodoList list = app.getRepository().getById(TodoList.class, listId);
            Map<String, String> substitutions = new HashMap<>();
            substitutions.put("/Lists/" + listId, list.getName());
            getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
        }
        getResourceContext().addAjaxNavigation("ajax", "Todo-Lists", ListsResource.class, TodosResource.class, "lid");
    }

    @Override
    public List<TodoChart> getEntity() {
        Filter filter = new Filter(getRequest(), DEFAULT_FILTER_EXPRESSION);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        filter.addEdgeOut("parent", "#" + listId);

        Pagination pagination = new Pagination(getRequest(), getResponse(), app.getRepository().getTodosCount(listId,
                filter));
        return app.getRepository().findAllTodos(filter, pagination).stream().map(t -> new TodoChart(t)).collect(Collectors.toList());
    }

//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostTodoResource.class, ArchivedTodosResource.class);
//    }

}
