package io.skysail.server.designer.demo.apps.demoapplication.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.apps.*;
import io.skysail.server.designer.demo.apps.demoapplication.*;

/**
 * generated from putResource.stg
 */
public class PutDemoApplicationResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.apps.demoapplication.DemoApplication> {


    protected String id;
    protected AppsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (AppsApplication)getApplication();
    }

    @Override
    public void updateEntity(DemoApplication  entity) {
        io.skysail.server.designer.demo.apps.demoapplication.DemoApplication original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.apps.demoapplication.DemoApplication getEntity() {
        return (io.skysail.server.designer.demo.apps.demoapplication.DemoApplication)app.getRepository(io.skysail.server.designer.demo.apps.demoapplication.DemoApplication.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(DemoApplicationsResourceGen.class);
    }
}