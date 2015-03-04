package io.skysail.server.app.todos.domain;

import java.util.Date;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutTodoResource extends PutEntityServerResource<Todo> {

    private String id;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public JSONObject getEntityAsJsonObject() {
        return null;// TodosRepository.getInstance().getById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        entity.setModified(new Date());
        TodosRepository.getInstance().update(entity);
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
