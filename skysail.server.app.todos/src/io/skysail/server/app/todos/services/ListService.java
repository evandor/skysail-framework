package io.skysail.server.app.todos.services;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.PostListResource;
import io.skysail.server.app.todos.repo.ListsRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class ListService {

    private ListsRepository repo;

    public ListService(ListsRepository listRepo) {
        this.repo = listRepo;
    }

    public TodoList getList(SkysailServerResource<?> resource, String listId) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        return app.getListRepo().findOne(listId);
    }

    public List<TodoList> getLists(SkysailServerResource<?> resource) {
        Filter filter = new Filter(resource.getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        Pagination pagination = new Pagination(resource.getRequest(), resource.getResponse(), repo.getListsCount(filter));
        return repo.findAllLists(filter, pagination);
    }

    public SkysailResponse<TodoList> addList(PostListResource resource, TodoList entity) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        handleDefaultList(resource, entity, app);
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        String id = repo.save(entity, "todos").getId().toString();
        entity.setId(id);
        return new SkysailResponse<>(null, entity);
    }

    public SkysailResponse<TodoList> updateList(SkysailServerResource<?> resource, TodoList entity) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        if (entity.isDefaultList()) {
            List<TodoList> usersDefaultLists = app.getUsersDefaultLists(resource.getRequest());
            app.removeDefaultFlag(usersDefaultLists);
        }

        TodoList original = getList(resource,resource.getAttribute(TodoApplication.LIST_ID));
        original.setName(entity.getName());
        original.setDesc(entity.getDesc());
        original.setDefaultList(entity.isDefaultList());
        original.setModified(new Date());
        app.getListRepo().update(resource.getAttribute(TodoApplication.LIST_ID), original, "todos");
        return new SkysailResponse<>();
    }

    public SkysailResponse delete(SkysailServerResource<?> resource, String listId) {
        TodoApplication app = (TodoApplication) resource.getApplication();
        TodoList todoList = app.getListRepo().findOne(listId);
        if (!todoList.getTodos().isEmpty()) {
            resource.getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, new IllegalStateException(),
                    "cannot delete list as it is not empty");
            return new SkysailResponse<String>();
        }
        app.getListRepo().delete(listId);
        return new SkysailResponse<String>();
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
