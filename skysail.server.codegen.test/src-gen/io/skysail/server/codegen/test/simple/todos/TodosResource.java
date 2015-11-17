package io.skysail.server.codegen.test.simple.todos;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class TodosResource extends ListServerResource<Todo> {

    private TodoApplication app;
    private TodoRepo repository;

    public TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
    }

    protected void doInit() {
        super.doInit();
        app = (TodoApplication)getApplication();
        repository = (TodoRepo) app.getRepository(Todo.class);
    }

    @Override
    public List<Todo> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(io.skysail.server.codegen.test.simple.todos.PostTodoResource.class);
    }
}
