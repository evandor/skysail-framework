package io.skysail.server.designer.checklist;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutListResource extends PutEntityServerResource<io.skysail.server.designer.checklist.List> {


    private String id;
    private ChecklistApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ChecklistApplication)getApplication();
    }

    @Override
    public SkysailResponse<io.skysail.server.designer.checklist.List> updateEntity(List  entity) {
        io.skysail.server.designer.checklist.List original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.checklist.List.class).update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.checklist.List getEntity() {
        return (io.skysail.server.designer.checklist.List)app.getRepository(io.skysail.server.designer.checklist.List.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }
}