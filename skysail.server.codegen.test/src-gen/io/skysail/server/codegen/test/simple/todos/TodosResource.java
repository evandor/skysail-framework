package io.skysail.server.codegen.test.simple.todos;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodosResource extends ListServerResource<Todo> {

    private io.skysail.server.codegen.test.simple.TodoApplication app;
    private TodoRepo repository;

    public TodosResource() {
        super(TodoResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Todos");
    }

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.codegen.test.simple.TodoApplication)getApplication();
        repository = (TodoRepo) app.getRepository(Todo.class);
    }

    @Override
    public List<Todo> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostTodoResource.class, TodosResource.class);
    }
}
