package io.skysail.server.designer.demo.apps.demovalueobject.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.apps.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;



/**
 * generated from postResourceNonAggregate.stg
 */
public class PostDemoValueObjectResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject> {

	private AppsApplication app;
    private Repository repository;

    public PostDemoValueObjectResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (AppsApplication) getApplication();
        repository = null;//app.getRepository(Space.class);
    }

    @Override
    public io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject createEntityTemplate() {
        return new DemoValueObject();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject entity) {
        Subject subject = SecurityUtils.getSubject();

        io.skysail.server.designer.demo.apps.demoapplication.DemoApplication entityRoot = (io.skysail.server.designer.demo.apps.demoapplication.DemoApplication) repository.findOne(getAttribute("id"));
        entityRoot.getDemoValueObjects().add(entity);
        repository.update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(DemoValueObjectsResourceGen.class);
    }
}