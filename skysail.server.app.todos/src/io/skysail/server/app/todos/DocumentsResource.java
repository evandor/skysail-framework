package io.skysail.server.app.todos;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.resources.TodoResource;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

public class DocumentsResource extends ListServerResource<SearchResult> {

    @Getter
    private String searchterm;

    private TodoApplication app;

    public DocumentsResource() {
        super(TodoResource.class);
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        searchterm = getQueryValue("searchterm");
        app = (TodoApplication)getApplication();
    }

    @Override
    public List<SearchResult> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        filter.add("title", searchterm);
        List<Todo> hits = app.getTodosRepo().findAllTodos(filter);
        return hits.stream().map(h -> new SearchResult(h)).collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(app.getMainLinks());
    }

}
