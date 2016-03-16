package io.skysail.server.designer.demo.organization.user.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.organization.*;
import io.skysail.server.designer.demo.organization.user.*;

/**
 * generated from postResource.stg
 */
public class PostUserResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.organization.user.User> {

	protected OrganizationApplication app;

    public PostUserResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (OrganizationApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.organization.user.User createEntityTemplate() {
        return new User();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.organization.user.User entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.organization.user.User.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(UsersResourceGen.class);
    }
}