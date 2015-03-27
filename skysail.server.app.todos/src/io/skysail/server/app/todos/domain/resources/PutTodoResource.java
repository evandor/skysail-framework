package io.skysail.server.app.todos.domain.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.domain.Todo;

import java.util.Date;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private String listId;
    private String todoId;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        listId = getAttribute(TodoApplication.LIST_ID);
        todoId = getAttribute(TodoApplication.TODO_ID);
        app = (TodoApplication) getApplication();
    }

    @Override
    public Todo getEntity() {
        return app.getRepository().getById(Todo.class, todoId);
    }

    @Override
    public JSONObject getEntityAsJsonObject() {
        return null;// TodosRepository.getInstance().getById(listId);
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        entity.setModified(new Date());
        app.getRepository().update(listId, entity);
        return null;
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }

}
