package io.skysail.server.app.designer.application.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.*;
import io.skysail.server.restlet.resources.ListServerResource;

public class ApplicationsResource extends ListServerResource<DbApplication> {

    public ApplicationsResource() {
        super(ApplicationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
    }

    @Override
    public List<DbApplication> getEntity() {
        DesignerApplication app = (DesignerApplication) getApplication();
        List<DbApplication> apps = app.getRepository().findAll(DbApplication.class);
        apps.stream().forEach(dbApp -> {
            ApplicationStatus status = app.getAppStatus().get(dbApp.getId().replace("#",""));
            dbApp.setStatus(status != null ? status : ApplicationStatus.UNDEFINED);
        });
        return apps;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostApplicationResource.class, UpdateBundleResource.class);
    }

}
