package io.skysail.server.designer.checklist;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutTemplateResource extends PutEntityServerResource<io.skysail.server.designer.checklist.Template> {


    private String id;
    private ChecklistApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ChecklistApplication)getApplication();
    }

    @Override
    public SkysailResponse<io.skysail.server.designer.checklist.Template> updateEntity(Template  entity) {
        io.skysail.server.designer.checklist.Template original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.checklist.Template.class).update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.checklist.Template getEntity() {
        return (io.skysail.server.designer.checklist.Template)app.getRepository(io.skysail.server.designer.checklist.Template.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TemplatesResource.class);
    }
}