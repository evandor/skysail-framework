package de.twenty11.skysail.server.app.tutorial.model2rest.step5;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.tutorial.model2rest.Model2RestTutorialApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TodoResource extends EntityServerResource<TodoModel> {

    private Integer id;

    public TodoResource() {
        addToContext(ResourceContextId.LINK_TITLE, "5. Extend Model");
    }

    @Override
    protected void doInit() throws ResourceException {
        String idAsString = getAttribute("id");
        if (idAsString != null) {
            id = Integer.parseInt(idAsString);
        }
    }

    @Override
    public TodoModel getEntity() {
        if (id == null) {
            return new TodoModel();
        }
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
    public List<Link> getLinks() {
        return ((Model2RestTutorialApplication) getApplication()).getAppNavigation(this);
    }

    @Override
    public String redirectTo() {
    	return super.redirectTo(Step5DemoResource.class);
    }

}
