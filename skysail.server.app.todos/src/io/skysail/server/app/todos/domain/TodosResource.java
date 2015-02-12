package io.skysail.server.app.todos.domain;

import java.util.List;
import java.util.function.Consumer;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

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
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return TodosRepository.getInstance().getTodos(principal.toString());
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostTodoResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> {
            l.substitute("id", id);
        };
    }
}
