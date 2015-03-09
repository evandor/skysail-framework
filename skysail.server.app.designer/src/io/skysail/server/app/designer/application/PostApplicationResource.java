package io.skysail.server.app.designer.application;

import io.skysail.server.app.designer.DesignerApplication;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostApplicationResource extends PostEntityServerResource<Application> {

    private DesignerApplication app;

    public PostApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Application");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
    }

    @Override
    public Application createEntityTemplate() {
        return new Application();
    }

    @Override
    public SkysailResponse<?> addEntity(Application entity) {
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
