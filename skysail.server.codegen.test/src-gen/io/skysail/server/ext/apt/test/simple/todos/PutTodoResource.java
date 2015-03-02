package io.skysail.server.ext.apt.test.simple.todos;

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
    public JSONObject getEntity() {
        return null;// TodosRepository.getInstance().getById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Todo entity) {
        TodosRepository.getInstance().update(entity);
        return null;
    }

}
