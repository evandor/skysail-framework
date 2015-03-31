package io.skysail.server.app.designer.application.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class PutApplicationResource extends PutEntityServerResource<Application> {

    private DesignerApplication app;
    private String id;

    protected void doInit() {
        super.doInit();
        id = getAttribute("id");
        app = (DesignerApplication) getApplication();
    }

    public Application getEntity() {
        return app.getRepository().getById(Application.class, id);
    }

    public SkysailResponse<?> updateEntity(Application entity) {
        app.getRepository().update(entity);
        return new SkysailResponse<String>();
    }
    
    @Override
    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        return super.redirectTo(ApplicationsResource.class);
    }

}
