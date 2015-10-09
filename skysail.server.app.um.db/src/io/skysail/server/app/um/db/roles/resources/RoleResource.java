package io.skysail.server.app.um.db.roles.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.Role;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class RoleResource extends EntityServerResource<Role> {

    private UmApplication app;

    protected void doInit() {
        app = (UmApplication) getApplication();
    }

    public Role getEntity() {
        return app.getRoleRepo().getById(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutRoleResource.class);
    }

    public SkysailResponse<Role> eraseEntity() {
        app.getRoleRepo().delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RolesResource.class);
    }
}
