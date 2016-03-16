package io.skysail.server.designer.demo.organization.user.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.organization.*;
import io.skysail.server.designer.demo.organization.user.*;

/**
 * generated from putResource.stg
 */
public class PutUserResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.organization.user.User> {


    protected String id;
    protected OrganizationApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (OrganizationApplication)getApplication();
    }

    @Override
    public void updateEntity(User  entity) {
        io.skysail.server.designer.demo.organization.user.User original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.organization.user.User.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.organization.user.User getEntity() {
        return (io.skysail.server.designer.demo.organization.user.User)app.getRepository(io.skysail.server.designer.demo.organization.user.User.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResourceGen.class);
    }
}