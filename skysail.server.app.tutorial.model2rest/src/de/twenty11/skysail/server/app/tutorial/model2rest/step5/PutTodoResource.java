package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutTodoResource extends PutEntityServerResource<TodoModel> {

	private int id;

	public PutTodoResource() {
		addToContext(ResourceContextId.LINK_TITLE, "update");
	}

	protected void doInit() throws ResourceException {
		id = Integer.parseInt(getAttribute("id"));
	}

	@Override
	public TodoModel getEntity() {
		return TodosRepository.getInstance().getById(id);
	}

	@Override
	public void updateEntity(TodoModel entity) {
	}

	@Override
	public String redirectTo() {
		return super.redirectTo(Step5DemoResource.class);
	}

}
