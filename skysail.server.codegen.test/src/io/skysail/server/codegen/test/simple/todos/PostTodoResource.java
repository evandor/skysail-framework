package io.skysail.server.codegen.test.simple.todos;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostTodoResource extends PostEntityServerResource<Todo> {

	private io.skysail.server.codegen.test.simple.TodoApplication app;

	public PostTodoResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
	protected void doInit() {
		app = (io.skysail.server.codegen.test.simple.TodoApplication)getApplication();
	}

	@Override
    public Todo createEntityTemplate() {
	    return new Todo();
    }

	@Override
	public String redirectTo() {
	    return null;//super.redirectTo(TodosResource.class);
	}
}