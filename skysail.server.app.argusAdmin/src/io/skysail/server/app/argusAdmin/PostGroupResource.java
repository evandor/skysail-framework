package io.skysail.server.app.argusAdmin;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostGroupResource extends PostEntityServerResource<io.skysail.server.app.argusAdmin.Group> {

	private ArgusAdminApplication app;

    public PostGroupResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ArgusAdminApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.argusAdmin.Group createEntityTemplate() {
        return new Group();
    }

    @Override
    public void addEntity(io.skysail.server.app.argusAdmin.Group entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.argusAdmin.Group.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(GroupsResource.class);
    }
}