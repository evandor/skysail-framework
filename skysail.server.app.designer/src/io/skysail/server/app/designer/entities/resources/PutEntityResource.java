package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Optional;

public class PutEntityResource extends PutEntityServerResource<Entity> {

    private DesignerApplication app;
    private String appId;
    private String entityId;

    protected void doInit() {
        super.doInit();
        appId = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        app = (DesignerApplication) getApplication();
    }

    public Entity getEntity() {
        // TODO same as in EntityResource
        Application application = app.getRepository().getById(Application.class, appId);
        Optional<Entity> optionalEntity = application.getEntities().stream().filter(e -> {
            return e.getName().equals(entityId);
        }).findFirst();
        return optionalEntity.orElse(null);
    }

    public SkysailResponse<?> updateEntity(Entity entity) {
        //app.getRepository().update(entity);
        return new SkysailResponse<String>();
    }

}