package io.skysail.server.app.designer.application;

import io.skysail.server.app.designer.DesignerApplication;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ApplicationsResource extends ListServerResource<Application> {

    private DesignerApplication app;

    public ApplicationsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
    }

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
    }

    @Override
    public List<Application> getEntity() {
        return app.getRepository().findAll(Application.class);
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostApplicationResource.class);
    }

}
