package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

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
        List<TodoList> lists = app.getRepository().findAllLists();
        TodoList rest = new TodoList();
        rest.setName("uncategorized");
        rest.setId("null");
        lists.add(rest);
        return lists;
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PostListResource.class);
    }

}
