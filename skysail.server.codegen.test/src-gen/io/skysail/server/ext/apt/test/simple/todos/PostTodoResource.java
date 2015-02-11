package io.skysail.server.ext.apt.test.simple.todos;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.simple.todos.*;


import de.twenty11.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.apt.test.simple.TodoGen;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ServerLink;

public class PostTodoResource extends PostEntityServerResource<Todo> {

    private String id;

	private TodoGen app;

	public PostTodoResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Todo");
    }

    @Override
	protected void doInit() throws ResourceException {
		app = (TodoGen)getApplication();
		id = getAttribute("id");
	}

	@Override
    public Todo createEntityTemplate() {
	    return new Todo();
    }

	@Override
    public SkysailResponse<?> addEntity(Todo entity) {
		entity = TodosRepository.getInstance().add(entity);
	    return new SkysailResponse<String>();
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(TodosResource.class);
	}
}