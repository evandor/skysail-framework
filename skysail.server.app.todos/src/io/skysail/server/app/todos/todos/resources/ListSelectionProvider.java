package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.forms.SelectionProvider;
import io.skysail.server.app.todos.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
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
        
        Filter filter = new Filter(resource.getRequest(), null);
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        List<TodoList> allLists = app.getRepository().findAllLists(filter, new Pagination());
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
