package io.skysail.server.app.um.db.permissions.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.Permission;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutPermissionResource extends PutEntityServerResource<Permission> {

    private UmApplication app;

    protected void doInit() {
        super.doInit();
        app = (UmApplication) getApplication();
    }

    public Permission getEntity() {
        return app.getPermissionRepo().getById(getAttribute("id"));
    }

    public SkysailResponse<Permission> updateEntity(Permission entity) {
        app.getPermissionRepo().update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PermissionsResource.class);
    }
}
