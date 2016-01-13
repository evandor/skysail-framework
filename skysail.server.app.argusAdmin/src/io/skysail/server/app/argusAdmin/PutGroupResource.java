package io.skysail.server.app.argusAdmin;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutGroupResource extends PutEntityServerResource<io.skysail.server.app.argusAdmin.Group> {


    private String id;
    private ArgusAdminApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ArgusAdminApplication)getApplication();
    }

    @Override
    public void updateEntity(Group  entity) {
        io.skysail.server.app.argusAdmin.Group original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.argusAdmin.Group.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.argusAdmin.Group getEntity() {
        return (io.skysail.server.app.argusAdmin.Group)app.getRepository(io.skysail.server.app.argusAdmin.Group.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(GroupsResource.class);
    }
}