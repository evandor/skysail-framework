package io.skysail.server.designer.demo.organization.user.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.designer.demo.organization.*;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from entityResource.stg
 */
public class UserResourceGen extends EntityServerResource<io.skysail.server.designer.demo.organization.user.User> {

    private String id;
    private OrganizationApplication app;
    private UserRepository repository;

    public UserResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (OrganizationApplication) getApplication();
        repository = (UserRepository) app.getRepository(io.skysail.server.designer.demo.organization.user.User.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.demo.organization.user.User getEntity() {
        return (io.skysail.server.designer.demo.organization.user.User)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutUserResourceGen.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResourceGen.class);
    }


}