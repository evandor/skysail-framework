package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.todos.*;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.utils.LinkUtils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class Top10TodosResource extends ListServerResource<TodoSummary> {

    private static final String DEFAULT_FILTER_EXPRESSION = "(!(status=" + Status.ARCHIVED + "))";
    protected TodoApplication app;

    public Top10TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "TOP 10 of Todos");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication) getApplication();
        getResourceContext().addAjaxNavigation(
                getResourceContext().getAjaxBuilder("lists-nav", "Lists:", ListsResource.class, TodosResource.class)
                .createLabel("new list")
                .createTarget(LinkUtils.fromResource(app, PostListResource.class).getUri())
                .nameProperty("name")
                .identifier("id").build());
//        getResourceContext().addAjaxNavigation("Lists:", ListsResource.class, TodosResource.class, "lid");
        // Map<String,String> substitutions = new HashMap<>();
        // substitutions.put("/Lists/" + listId, list.getName());
        // getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(),
        // substitutions);
    }

    @Override
    public List<TodoSummary> getEntity() {
        Filter filter = new Filter(getRequest(), DEFAULT_FILTER_EXPRESSION);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());

        Pagination pagination = new Pagination(getRequest(), getResponse(), 10);
        List<Todo> todos = app.getRepository().findAllTodos(filter, pagination);
        return todos.stream().map(todo -> {
            return new TodoSummary(todo);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ListsResource.class, PostTodoWoListResource.class);
    }

}
