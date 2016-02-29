package de.twenty11.skysail.server.app.tutorial.model2rest.step4;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;

public class TodoResource extends EntityServerResource<TodoModel4> {

    private Integer id;

    public TodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "4. Check Sanitization");
    }

    @Override
    protected void doInit() throws ResourceException {
        String idAsString = getAttribute("id");
        if (idAsString != null) {
            id = Integer.parseInt(idAsString);
        }
    }

    @Override
    public TodoModel4 getEntity() {
        if (id == null) {
            return new TodoModel4();
        }
        return Step4ModelsRepository.getInstance().getById(id);
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
        return ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
    }

}
