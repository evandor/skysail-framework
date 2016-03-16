package io.skysail.server.designer.demo.apps.demoapplication.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.apps.*;
import io.skysail.server.designer.demo.apps.demoapplication.*;

/**
 * generated from postResource.stg
 */
public class PostDemoApplicationResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.apps.demoapplication.DemoApplication> {

	protected AppsApplication app;

    public PostDemoApplicationResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (AppsApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.apps.demoapplication.DemoApplication createEntityTemplate() {
        return new DemoApplication();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(DemoApplicationsResourceGen.class);
    }
}