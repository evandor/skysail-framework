package io.skysail.server.app.oEService.user.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.app.oEService.*;

import io.skysail.server.app.oEService.oe.*;
import io.skysail.server.app.oEService.oe.resources.*;
import io.skysail.server.app.oEService.user.*;
import io.skysail.server.app.oEService.user.resources.*;


/**
 * generated from entityResource.stg
 */
public class UserResourceGen extends EntityServerResource<io.skysail.server.app.oEService.user.User> {

    private String id;
    private OEServiceApplication app;
    private UserRepository repository;

    public UserResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (OEServiceApplication) getApplication();
        repository = (UserRepository) app.getRepository(io.skysail.server.app.oEService.user.User.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.oEService.user.User getEntity() {
        return (io.skysail.server.app.oEService.user.User)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutUserResourceGen.class,PostOEResourceGen.class,OEsResourceGen.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResourceGen.class);
    }


}