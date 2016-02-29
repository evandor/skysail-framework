package de.twenty11.skysail.server.app.tutorial.model2rest.step2;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;

public class TodoResource extends EntityServerResource<TodoModel2> {

    public TodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "2. Add Validation");
    }

    @Override
    public TodoModel2 getEntity() {
        return new TodoModel2();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
         List<Link> linkheader = ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
//         linkheader.add(new Link.Builder("step1").title("<<").build());
//         linkheader.add(new Link.Builder("/tutorialM2R").title("Tutorial Home").build());
//         linkheader.add(new Link.Builder("step3").title(">>").build());
         return linkheader;
    }

}
