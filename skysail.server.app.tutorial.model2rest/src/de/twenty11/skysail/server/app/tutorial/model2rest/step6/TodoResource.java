package de.twenty11.skysail.server.app.tutorial.model2rest.step6;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodoResource extends EntityServerResource<TodoModel6> {

    private Integer id;

    public TodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "6. Check Integration");
    }

    @Override
    protected void doInit() throws ResourceException {
        String idAsString = getAttribute("id");
        if (idAsString != null) {
            id = Integer.parseInt(idAsString);
        }
    }

    @Override
    public TodoModel6 getEntity() {
        if (id == null) {
            return new TodoModel6();
        }
        return Step6ModelsRepository.getInstance().getById(id);
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
        Link.add(new Link.Builder("step3").title("<<").build());
        Link.add(new Link.Builder("/tutorialM2R").title("Tutorial Home").build());
        Link.add(new Link.Builder("step5").title(">>").build());
        return Link;
    }

}
