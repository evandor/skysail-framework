package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostEntityResource extends PostEntityServerResource<Entity> {

    private DesignerApplication app;
    private String id;

    public PostEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Entity");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
    }

    @Override
    public Entity createEntityTemplate() {
        return new Entity();
    }

    @Override
    public SkysailResponse<?> addEntity(Entity entity) {
        Application application = app.getRepository().getById(Application.class, id);
        application.getEntities().add(entity);
        app.getRepository().update(application);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesResource.class);
    }
}
