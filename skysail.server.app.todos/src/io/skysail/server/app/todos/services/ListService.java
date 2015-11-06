package io.skysail.server.app.todos.services;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.ListsRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

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

    public SkysailResponse<TodoList> addList(PostListResource resource, TodoList entity) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        handleDefaultList(resource, entity, app);
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        String id = repo.save(entity, "todos").toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

    private void handleDefaultList(PostListResource resource, TodoList entity, TodoApplication app) {
        List<TodoList> usersDefaultLists = app.getUsersDefaultLists(resource.getRequest());
        if (usersDefaultLists.isEmpty()) {
            entity.setDefaultList(true);
        } else {
            app.removeDefaultFlag(usersDefaultLists);
        }
    }

}
