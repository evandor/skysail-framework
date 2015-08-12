package io.skysail.server.ext.apt.test.simple.todos;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.simple.todos.*;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodosResource extends ListServerResource<Todo> {

    private String id;

    public TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public List<Todo> getData() {
        return TodosRepository.getInstance().getTodos();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostTodoResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> { l.substitute("id", id); };
    }
}