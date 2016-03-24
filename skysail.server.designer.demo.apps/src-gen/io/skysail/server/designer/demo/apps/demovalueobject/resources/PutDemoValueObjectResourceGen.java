package io.skysail.server.designer.demo.apps.demovalueobject.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.apps.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;

/**
 * generated from putResource.stg
 */
public class PutDemoValueObjectResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject> {


    protected String id;
    protected AppsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (AppsApplication)getApplication();
    }

    @Override
    public void updateEntity(DemoValueObject  entity) {
        io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject getEntity() {
        return (io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject)app.getRepository(io.skysail.server.designer.demo.apps.demovalueobject.DemoValueObject.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(DemoValueObjectsResourceGen.class);
    }
}