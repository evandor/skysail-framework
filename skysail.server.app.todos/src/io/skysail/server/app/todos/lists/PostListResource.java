package io.skysail.server.app.todos.lists;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.resources.Top10TodosResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostListResource extends PostEntityServerResource<TodoList> {

    public PostListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new List");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        getResourceContext().addDisabledAjaxNavigation("Todo-Lists", ListsResource.class);
    }

    @Override
    public TodoList createEntityTemplate() {
        return new TodoList();
    }

    @Override
    public SkysailResponse<?> addEntity(TodoList entity) {
        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        subject.getPrincipals().getPrimaryPrincipal();
        entity.setOwner(subject.getPrincipal().toString());
        String id = TodosRepository.add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>();
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
