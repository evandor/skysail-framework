package io.skysail.server.codegen.test.simple.todos;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutTodoResource extends PutEntityServerResource<Todo> {

    protected TodoApplication app;
    protected TodoRepo repository;

	protected void doInit() {
        super.doInit();
        app = (TodoApplication) getApplication();
        repository = (TodoRepo) app.getRepository(Todo.class);
    }

    public Todo getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public void updateEntity(Todo entity) {
        repository.update(entity, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }
}
