package io.skysail.server.app.oEService.user.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.oEService.*;
import io.skysail.server.app.oEService.user.*;

/**
 * generated from putResource.stg
 */
public class PutUserResourceGen extends PutEntityServerResource<io.skysail.server.app.oEService.user.User> {


    protected String id;
    protected OEServiceApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (OEServiceApplication)getApplication();
    }

    @Override
    public void updateEntity(User  entity) {
        io.skysail.server.app.oEService.user.User original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.oEService.user.User.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.oEService.user.User getEntity() {
        return (io.skysail.server.app.oEService.user.User)app.getRepository(io.skysail.server.app.oEService.user.User.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResourceGen.class);
    }
}