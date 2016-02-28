package io.skysail.server.app.snap;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.snap.SnapApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutModelResource extends PutEntityServerResource<io.skysail.server.app.snap.Model> {


    protected String id;
    protected SnapApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (SnapApplication)getApplication();
    }

    @Override
    public void updateEntity(Model  entity) {
        io.skysail.server.app.snap.Model original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.snap.Model.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.snap.Model getEntity() {
        return (io.skysail.server.app.snap.Model)app.getRepository(io.skysail.server.app.snap.Model.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ModelsResource.class);
    }
}