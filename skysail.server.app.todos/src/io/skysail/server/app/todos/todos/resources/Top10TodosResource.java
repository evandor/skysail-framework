package io.skysail.server.app.todos.todos.resources;

import io.skysail.server.app.todos.todos.*;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class Top10TodosResource extends TodoSummaryResource {

    private static final String DEFAULT_FILTER_EXPRESSION = "(!(status=" + Status.DONE + "))";

    public Top10TodosResource() {
        super(TodoResource.class);
        setDescription("Returns the Users Top 10 Todos.");
        addToContext(ResourceContextId.LINK_TITLE, "TOP 10 of Todos");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    public List<TodoSummary> getEntity() {
        Filter filter = new Filter(getRequest(), DEFAULT_FILTER_EXPRESSION);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());

        Pagination pagination = new Pagination(getRequest(), getResponse(), 10);
        List<Todo> todos = app.getTodosRepo().findAllTodos(filter, pagination);
        return todos.stream().map(todo -> new TodoSummary(todo)).collect(Collectors.toList());
    }

}
