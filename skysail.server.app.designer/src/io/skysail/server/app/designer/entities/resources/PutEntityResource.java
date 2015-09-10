package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.PutEntityServerResource;

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
        //Application application = app.getRepository().getById(Application.class, appId);
        return app.getEntity(entityId);
//        System.out.println(application.getEntities());
//        Optional<Entity> optionalEntity = application.getEntities().stream().filter(e -> {
//            if (e == null || e.getId() == null) {
//                return false;
//            }
//            return e.getId().replace("#", "").equals(entityId);
//        }).findFirst();
//        return optionalEntity.orElse(null);
    }

    public SkysailResponse<Entity> updateEntity(Entity entity) {
        app.getRepository().update(entity);
        return new SkysailResponse<>();
    }

}
