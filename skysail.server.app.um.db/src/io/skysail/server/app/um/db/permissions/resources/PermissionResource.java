package io.skysail.server.app.um.db.permissions.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.Permission;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class PermissionResource extends EntityServerResource<Permission> {

    private UmApplication app;

    protected void doInit() {
        app = (UmApplication) getApplication();
    }

    public Permission getEntity() {
        return app.getPermissionRepo().findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutPermissionResource.class);
    }

    public SkysailResponse<Permission> eraseEntity() {
        app.getPermissionRepo().delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PermissionsResource.class);
    }
}
