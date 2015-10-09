package io.skysail.server.app.um.db.roles.resources;

import io.skysail.server.app.um.db.domain.Role;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostRoleResource extends PostEntityServerResource<Role> {

    @Override
    public Role createEntityTemplate() {
        return new Role();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RolesResource.class);
    }
}

