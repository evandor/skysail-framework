package de.twenty11.skysail.server.app.tutorial.model2rest.step3;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;

public class TodoResource extends EntityServerResource<TodoModel3> {

    private Integer id;

    public TodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "3. Add Persistence");
    }

    @Override
    protected void doInit() throws ResourceException {
        String idAsString = getAttribute("id");
        if (idAsString != null) {
            id = Integer.parseInt(idAsString);
        }
    }

    @Override
    public TodoModel3 getEntity() {
        if (id == null) {
            return new TodoModel3();
        }
        return Step3ModelsRepository.getInstance().getById(id);
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
        List<Link> Link = ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
       // Link.add(new Link.Builder("step2").title("<<").build());
//        Link.add(new Link.Builder("/tutorialM2R").title("Tutorial Home").build());
//        Link.add(new Link.Builder("step4").title(">>").build());
        return Link;
    }

}
