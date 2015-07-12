package io.skysail.server.app.todos.lists;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.Date;

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
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }

}
