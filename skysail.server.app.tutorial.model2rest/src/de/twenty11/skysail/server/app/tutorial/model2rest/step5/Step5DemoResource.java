package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class Step5DemoResource extends ListServerResource<TodoModel> {

	public Step5DemoResource() {
		super(TodoResource.class);
	}

    @Override
    public List<TodoModel> getEntity() {
        return TodosRepository.getInstance().findAll();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostTodoResource.class);
    }


}
