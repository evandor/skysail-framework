package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.resources.Top10TodosResource;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Slf4j
public class PostListResource extends PostEntityServerResource<TodoList> {

    private TodoApplication app;

    public PostListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new List");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (TodoApplication)getApplication();
        getResourceContext().addDisabledAjaxNavigation("Todo-Lists", ListsResource.class);
    }

    @Override
    public TodoList createEntityTemplate() {
        return new TodoList();
    }

    @Override
    public SkysailResponse<?> addEntity(TodoList entity) {
        if (entity.isDefaultList()) {
            clearUsersOtherLists();
        }
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        subject.getPrincipals().getPrimaryPrincipal();
        entity.setOwner(subject.getPrincipal().toString());
        String id = TodosRepository.add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>();
    }

    private void clearUsersOtherLists() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        filter.add("defaultList", "true");

        List<TodoList> usersDefaultsList = app.getRepository().findAllLists(filter, null);
        for (TodoList todoList : usersDefaultsList) {
            todoList.setDefaultList(false);
            log.info("removing default-List Flag from todo list with id '{}'", todoList.getId());
            app.getRepository().update(todoList.getId(), todoList);
        }

    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(Top10TodosResource.class, ListsResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}
