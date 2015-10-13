package io.skysail.server.app.um.db.permissions.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.Permission;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PermissionsResource extends ListServerResource<Permission> {

    private UmApplication app;

    public PermissionsResource() {
        super(PermissionResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Permissions");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (UmApplication) getApplication();
    }

    @Override
    public List<Permission> getEntity() {
      return app.getPermissionRepo().find(new Filter(getRequest()));
    }

    @Override
    public List<Link> getLinks() {
       return super.getLinks(PostPermissionResource.class);
    }


}
