package io.skysail.server.app.um.db.roles.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.Role;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutRoleResource extends PutEntityServerResource<Role> {

    private UmApplication app;

    protected void doInit() {
        super.doInit();
        app = (UmApplication) getApplication();
    }

    public Role getEntity() {
        return app.getRoleRepo().findOne(getAttribute("id"));
    }

    public SkysailResponse<Role> updateEntity(Role entity) {
        app.getRoleRepo().update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RolesResource.class);
    }
}
