package de.twenty11.skysail.server.app.tutorial.model2rest.step1;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodoResource extends EntityServerResource<TodoModel1> {

    public TodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "1. Create Model");
    }

    @Override
    public TodoModel1 getEntity() {
        return new TodoModel1();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Link> getLinks() {
         List<Link> linkheaders = ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
         //linkheaders.add(new Link.Builder("step2").title("Next step: Add Validation").build());
         return linkheaders;

    }

}
