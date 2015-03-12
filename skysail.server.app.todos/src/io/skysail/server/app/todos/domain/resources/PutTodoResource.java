package io.skysail.server.app.todos.domain.resources;

import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.domain.Todo;

import java.util.Date;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private String id;
    private TodoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (TodoApplication) getApplication();
    }

    @Override
    public JSONObject getEntityAsJsonObject() {
        return null;// TodosRepository.getInstance().getById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        entity.setModified(new Date());
        app.getRepository().update(entity);
        return null;
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }

    @Override
    public Todo getEntity() {
        return null;
    }
}