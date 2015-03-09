package io.skysail.server.app.designer.application;

import io.skysail.server.app.designer.DesignerApplication;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutApplicationResource extends PutEntityServerResource<Application> {

    private DesignerApplication app;
    private String id;

    protected void doInit() {
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

}
