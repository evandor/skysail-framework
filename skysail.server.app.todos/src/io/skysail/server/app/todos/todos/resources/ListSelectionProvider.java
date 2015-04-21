package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.forms.SelectionProvider;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

public class ListSelectionProvider implements SelectionProvider {

    public static ListSelectionProvider getInstance() {
        return new ListSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        SkysailServerResource<?> ssr = (SkysailServerResource<?>) resource;
        TodoApplication app = (TodoApplication) ssr.getApplication();
        List<TodoList> allLists = app.getRepository().findAllLists();
        return allLists.stream().collect(Collectors.toMap(TodoList::getId, TodoList::getName));
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
