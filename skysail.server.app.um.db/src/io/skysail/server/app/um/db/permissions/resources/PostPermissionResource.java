package io.skysail.server.app.um.db.permissions.resources;

import io.skysail.server.app.um.db.domain.Permission;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostPermissionResource extends PostEntityServerResource<Permission> {

    @Override
    public Permission createEntityTemplate() {
        return new Permission();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PermissionsResource.class);
    }
}

