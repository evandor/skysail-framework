package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.todos.Todo;

import java.util.List;
import java.util.function.Consumer;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.Header;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.utils.HeadersUtils;

public class TodosResource extends ListServerResource<Todo> {

    private String listId;
    private TodoApplication app;
    private int page = 1;

    public TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
    }

    @Override
    protected void doInit() throws ResourceException {
        listId = getAttribute(TodoApplication.LIST_ID);
        app = (TodoApplication) getApplication();
    }

    @Override
    public List<Todo> getEntity() {
        int linesPerPage = 10;
        String username = SecurityUtils.getSubject().getPrincipal().toString();

        Series<Header> headers = HeadersUtils.getHeaders(getResponse());
        long clipCount = app.getRepository().getTodosCount(username,listId);
        headers.add(new Header(HeadersUtils.PAGINATION_PAGES, Long.toString(1 + Math.floorDiv(clipCount, linesPerPage))));
        headers.add(new Header(HeadersUtils.PAGINATION_PAGE, Integer.toString(page)));
        headers.add(new Header(HeadersUtils.PAGINATION_HITS, Long.toString(clipCount)));

        return app.getRepository().findAll(Todo.class, listId, "ORDER BY rank ASC");
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PostTodoResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            l.substitute(TodoApplication.LIST_ID, listId);
        };
    }

}
