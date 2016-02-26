package io.skysail.server.app.bpmnmodeler;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutModelResource extends PutEntityServerResource<io.skysail.server.app.bpmnmodeler.Model> {


    protected String id;
    protected BpmnModelerApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (BpmnModelerApplication)getApplication();
    }

    @Override
    public void updateEntity(Model  entity) {
        io.skysail.server.app.bpmnmodeler.Model original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.bpmnmodeler.Model.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.bpmnmodeler.Model getEntity() {
        return (io.skysail.server.app.bpmnmodeler.Model)app.getRepository(io.skysail.server.app.bpmnmodeler.Model.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ModelsResource.class);
    }
}