package io.skysail.server.designer.checklist;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutResource extends PutEntityServerResource<> {


    private String id;
    private ChecklistApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ChecklistApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(  entity) {
        io.skysail.server.designer.checklist.List original = getEntity();
        copyProperties(original,entity);

        app.getRepository().update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public  getEntity() {
        return app.getRepository().getById(.class, id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(sResource.class);
    }
}