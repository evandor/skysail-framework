package io.skysail.server.app.oEService;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostUserResource extends PostEntityServerResource<io.skysail.server.app.oEService.User> {

	protected OEServiceApplication app;

    public PostUserResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (OEServiceApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.oEService.User createEntityTemplate() {
        return new User();
    }

    @Override
    public void addEntity(io.skysail.server.app.oEService.User entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.oEService.User.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResource.class);
    }
}