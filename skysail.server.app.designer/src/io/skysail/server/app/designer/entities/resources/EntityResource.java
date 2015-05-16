package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class EntityResource extends EntityServerResource<Entity> {

    private String appId;
    private String entityId;
    private DesignerApplication app;

    protected void doInit() {
        appId = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        app = (DesignerApplication) getApplication();
    }

    public Entity getEntity() {
        Application application = app.getRepository().getById(Application.class, appId);
        return app.getEntity(application, entityId);
    }

    public List<Link> getLinks() {
        return super.getLinks(PutEntityResource.class, EntityResource.class, PostFieldResource.class, FieldsResource.class, PostSubEntityResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
