package io.skysail.server.app.designer.application.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ApplicationsResource extends ListServerResource<DbApplication> {

    private DesignerApplication app;

    public ApplicationsResource() {
        super(ApplicationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        getResourceContext().addAjaxNavigation("ajax", "Applications:", ApplicationsResource.class, ApplicationResource.class, "id");
    }

    @Override
    public List<DbApplication> getEntity() {
        return app.getRepository().findAll(DbApplication.class);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostApplicationResource.class);
    }

}
