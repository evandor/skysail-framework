package io.skysail.server.app.designer.entities;

import io.skysail.server.app.designer.DesignerApplication;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostEntityResource extends PostEntityServerResource<Entity> {

    private DesignerApplication app;

    public PostEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Entity");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
    }

    @Override
    public Entity createEntityTemplate() {
        return new Entity();
    }

    @Override
    public SkysailResponse<?> addEntity(Entity entity) {
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
