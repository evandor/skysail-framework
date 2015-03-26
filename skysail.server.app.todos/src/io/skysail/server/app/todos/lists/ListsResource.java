package io.skysail.server.app.todos.lists;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class ListsResource extends ListServerResource<TodoList> {

    @Override
    public List<TodoList> getEntity() {
        return null;
    }

}
