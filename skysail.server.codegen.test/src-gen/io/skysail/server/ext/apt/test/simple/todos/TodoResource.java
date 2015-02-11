package io.skysail.server.ext.apt.test.simple.todos;

import java.util.List;

import org.restlet.resource.ResourceException;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class TodoResource extends EntityServerResource<Todo> {

	private String id;

	@Override
	protected void doInit() throws ResourceException {
	    id = getAttribute("id");
	}

	@Override
	public Todo getData() {
		return TodosRepository.getInstance().getById(id);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
        TodosRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
	}

    @Override
	public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutTodoResource.class);
	}

}
