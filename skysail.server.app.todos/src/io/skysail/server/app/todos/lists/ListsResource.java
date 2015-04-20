package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.resources.PostTodoWoListResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class ListsResource extends ListServerResource<TodoList> {

    private TodoApplication app;

    public ListsResource() {
        super(ListResource.class);
    }

    @Override
    protected void doInit() {
        app = (TodoApplication) getApplication();
    }

    @Override
    public List<TodoList> getEntity() {
        return app.getRepository().findAllLists();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostListResource.class, PostTodoWoListResource.class);
    }

}
