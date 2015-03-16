package io.skysail.server.app.todos.domain.resources;

import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.domain.Todo;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    private TodoApplication app;

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (TodoApplication) getApplication();
        app = (TodoApplication) getApplication();
    }

    @Override
    public Todo createEntityTemplate() {
        return new Todo();
    }

    @Override
    public SkysailResponse<?> addEntity(Todo entity) {
        entity.setCreated(new Date());
        entity.setOwner(SecurityUtils.getSubject().getPrincipal().toString());
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }
}