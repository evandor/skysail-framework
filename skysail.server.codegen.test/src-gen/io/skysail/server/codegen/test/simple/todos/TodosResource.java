package io.skysail.server.codegen.test.simple.todos;

import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodosResource extends ListServerResource<Todo> {

    public TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
    }

    @Override
    public List<Todo> getEntity() {
        return null;
    }
}
