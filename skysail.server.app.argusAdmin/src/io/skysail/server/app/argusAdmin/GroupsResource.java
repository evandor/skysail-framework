package io.skysail.server.app.argusAdmin;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class GroupsResource extends ListServerResource<io.skysail.server.app.argusAdmin.Group> {

    private ArgusAdminApplication app;
    private GroupRepository repository;

    public GroupsResource() {
        super(GroupResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Groups");
    }

    @Override
    protected void doInit() {
        app = (ArgusAdminApplication) getApplication();
        repository = (GroupRepository) app.getRepository(io.skysail.server.app.argusAdmin.Group.class);
    }

    @Override
    public List<io.skysail.server.app.argusAdmin.Group> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
       return super.getLinks(PostGroupResource.class);
    }
}