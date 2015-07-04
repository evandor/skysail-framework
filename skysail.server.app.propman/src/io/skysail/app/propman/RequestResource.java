package io.skysail.app.propman;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class RequestResource  extends EntityServerResource<Request > {

    private String id;
    private PropManApplication app;

    public RequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PropManApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
       /* TodoList todoList = app.getRepository().getById(Request.class, listId);
        if (todoList.getTodosCount() > 0) {
            // TODO revisit: make a business violation from that
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, new IllegalStateException(),
                    "cannot delete list as it is not empty");
            return new SkysailResponse<String>();
        }
        app.getRepository().delete(TodoList.class, listId);*/
        return new SkysailResponse<String>();
    }

    @Override
    public Request getEntity() {
        return app.getRepository().getById(Request.class, id);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutRequestResource.class);
    }
}
