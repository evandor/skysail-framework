package de.twenty11.skysail.server.app.tutorial.model2rest.step4;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class Step4DemoResource extends ListServerResource<TodoModel4> {

    @Override
    public List<TodoModel4> getEntity() {
        return Step4ModelsRepository.getInstance().findAll();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(de.twenty11.skysail.server.app.tutorial.model2rest.step4.PostTodoResource.class);
    }


}
