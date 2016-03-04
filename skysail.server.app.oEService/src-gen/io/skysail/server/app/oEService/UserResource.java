package io.skysail.server.app.oEService;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from entityResource.stg
 */
public class UserResource extends EntityServerResource<io.skysail.server.app.oEService.User> {

    private String id;
    private OEServiceApplication app;
    private UserRepository repository;

    public UserResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (OEServiceApplication) getApplication();
        repository = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.oEService.User getEntity() {
        return (io.skysail.server.app.oEService.User)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutUserResource.class,PostOEResource.class,OEsResource.class);
    }

}