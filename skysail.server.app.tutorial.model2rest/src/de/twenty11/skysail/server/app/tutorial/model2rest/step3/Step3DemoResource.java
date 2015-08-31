package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class Step3DemoResource extends ListServerResource<TodoModel3> {

    @Override
    public List<TodoModel3> getEntity() {
        return Step3ModelsRepository.getInstance().findAll();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(de.twenty11.skysail.server.app.tutorial.model2rest.step3.PostTodoResource.class);
    }


}
