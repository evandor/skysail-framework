package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.List;

import org.apache.shiro.SecurityUtils;

public class ArchivedTodosResource extends TodosResource {

    public ArchivedTodosResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Archived Todos");
        addToContext(ResourceContextId.LINK_GLYPH, "th-list");
    }

    @Override
    public List<Todo> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner",  SecurityUtils.getSubject().getPrincipal().toString());
        filter.add("list", listId);
        filter.add("status", Status.DONE.name());

        Pagination pagination = new Pagination(getRequest(), getResponse(), app.getTodosRepo().getTodosCount(listId, filter));
        return app.getTodosRepo().findAllTodos(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class, ArchivedTodosResource.class);
    }


}
