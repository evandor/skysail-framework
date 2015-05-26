package io.skysail.server.app.designer.application.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.codegen.PostCompilationResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

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
        return app.getRepository().findAll(Application.class);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostApplicationResource.class, PostCompilationResource.class);
    }

}
