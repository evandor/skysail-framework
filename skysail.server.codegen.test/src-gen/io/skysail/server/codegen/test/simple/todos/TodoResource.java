package io.skysail.server.codegen.test.simple.todos;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class TodoResource extends EntityServerResource<Todo> {

    private TodoApplication app;
    private TodoRepo repository;

    protected void doInit() {
        super.doInit();
        app = (TodoApplication)getApplication();
        repository = (TodoRepo) app.getRepository(Todo.class);
    }

    public Todo getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutTodoResource.class);
    }

    public SkysailResponse<Todo> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}
