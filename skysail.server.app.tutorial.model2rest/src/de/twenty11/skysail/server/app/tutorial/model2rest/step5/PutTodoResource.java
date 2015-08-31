package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

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
	public SkysailResponse<?> updateEntity(TodoModel entity) {
		return null;
	}

	@Override
	public String redirectTo() {
		return super.redirectTo(Step5DemoResource.class);
	}

}
