package io.skysail.server.app.argusAdmin;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class GroupResource extends EntityServerResource<io.skysail.server.app.argusAdmin.Group> {

    private String id;
    private ArgusAdminApplication app;
    private GroupRepository repository;

    public GroupResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (ArgusAdminApplication) getApplication();
        repository = (GroupRepository) app.getRepository(io.skysail.server.app.argusAdmin.Group.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.argusAdmin.Group getEntity() {
        return (io.skysail.server.app.argusAdmin.Group)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutGroupResource.class);
    }

}