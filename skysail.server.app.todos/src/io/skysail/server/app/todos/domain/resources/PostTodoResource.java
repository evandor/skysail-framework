package io.skysail.server.app.todos.domain.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.domain.Todo;

import java.util.Date;

import org.apache.shiro.SecurityUtils;

import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    private TodoApplication app;

    public PostTodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
    protected void doInit() {
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
        if ("submitAndNew".equals(submitValue)) {
            return super.redirectTo(PostTodoResource.class);
        }
        return super.redirectTo(TodosResource.class);
    }
}