package io.skysail.server.codegen.test.simple.todos;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutTodoResource extends PutEntityServerResource<Todo> {

    private io.skysail.server.codegen.test.simple.TodoApplication app;
    private TodoRepo repository;

	protected void doInit() {
        super.doInit();
        app = (io.skysail.server.codegen.test.simple.TodoApplication) getApplication();
        repository = (TodoRepo) app.getRepository(Todo.class);
    }

    public Todo getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Todo> updateEntity(Todo entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodosResource.class);
    }
}
