package io.skysail.server.codegen.test.simple.todos;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;


import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class TodoResource extends EntityServerResource<Todo> {

     private io.skysail.server.codegen.test.simple.TodoApplication app;

    protected void doInit() {
        app = (io.skysail.server.codegen.test.simple.TodoApplication)getApplication();
    }

    public Todo getEntity() {
        return app.getTodoRepo().findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutTodoResource.class);
    }

    public SkysailResponse<Todo> eraseEntity() {
        app.getTodoRepo().delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader(PutTodoResource.class);
    }
}