package io.skysail.server.app.todos.lists;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostListResource extends PostEntityServerResource<TodoList> {

    private TodoApplication app;

    public PostListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new List");
    }
    
    @Override
    protected void doInit() throws ResourceException {
        app = (TodoApplication) getApplication();
    }

    @Override
    public TodoList createEntityTemplate() {
        return new TodoList();
    }

    @Override
    public SkysailResponse<?> addEntity(TodoList entity) {
        app.getRepository().add(entity);
        return new SkysailResponse<>();
    }

}
