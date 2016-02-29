package io.skysail.server.app.um.db.roles.resources;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.app.um.db.domain.Role;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RolesResource extends ListServerResource<Role> {

    private UmApplication app;

    public RolesResource() {
        super(RoleResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Roles");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (UmApplication) getApplication();
    }

    @Override
    public List<Role> getEntity() {
      return app.getRoleRepo().find(new Filter(getRequest()));
    }

    @Override
    public List<Link> getLinks() {
       return super.getLinks(PostRoleResource.class);
    }


}
