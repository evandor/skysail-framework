package io.skysail.server.app.designer.application;

import io.skysail.server.app.designer.DesignerApplication;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ApplicationResource extends EntityServerResource<Application> {

    private String id;
    private DesignerApplication app;

    public ApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "show");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (DesignerApplication) getApplication();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Application getEntity() {
        return app.getRepository().getById(Application.class, id);
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutApplicationResource.class, ApplicationResource.class);
    }

}
