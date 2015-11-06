package io.skysail.server.app.todos.services;

import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.repo.ListsRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.List;

import org.apache.shiro.SecurityUtils;

public class ListService {

    private ListsRepository repo;

    public ListService(ListsRepository listRepo) {
        this.repo = listRepo;
    }

    public List<TodoList> getLists(ListsResource listsResource) {
        Filter filter = new Filter(listsResource.getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        Pagination pagination = new Pagination(listsResource.getRequest(), listsResource.getResponse(), repo.getListsCount(filter));
        return repo.findAllLists(filter, pagination);
    }

}
