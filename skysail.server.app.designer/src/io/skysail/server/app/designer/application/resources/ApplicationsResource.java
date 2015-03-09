package io.skysail.server.app.designer.application.resources;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ApplicationsResource extends ListServerResource<Application> {

    private DesignerApplication app;

    public ApplicationsResource() {
        super(ApplicationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
    }

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
    }

    @Override
    public List<Application> getEntity() {
        List<Application> all = app.getRepository().findAll(Application.class);
        // all.stream().forEach(a -> System.out.println(a.getEntities()));
        return all;
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostApplicationResource.class);
    }

}
