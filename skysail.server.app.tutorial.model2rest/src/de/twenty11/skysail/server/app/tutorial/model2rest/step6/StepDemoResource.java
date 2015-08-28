package de.twenty11.skysail.server.app.tutorial.model2rest.step6;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class StepDemoResource extends ListServerResource<TodoModel6> {

    @Override
    public List<TodoModel6> getEntity() {
        return Step6ModelsRepository.getInstance().findAll();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(de.twenty11.skysail.server.app.tutorial.model2rest.step6.PostTodoResource.class);
    }


}
