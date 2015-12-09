package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutEntityResource extends PutEntityServerResource<DbEntity> {

    private DesignerApplication app;
    private String appId;
    private String entityId;

    protected void doInit() {
        super.doInit();
        appId = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        app = (DesignerApplication) getApplication();
    }

    public DbEntity getEntity() {
        //DbApplication application = app.getRepository().getById(DbApplication.class, appId);
        return app.getEntity(entityId);
//        System.out.println(application.getEntities());
//        Optional<DbEntity> optionalEntity = application.getEntities().stream().filter(e -> {
//            if (e == null || e.getId() == null) {
//                return false;
//            }
//            return e.getId().replace("#", "").equals(entityId);
//        }).findFirst();
//        return optionalEntity.orElse(null);
    }

    public SkysailResponse<DbEntity> updateEntity(DbEntity entity) {
        app.getRepository().update(entity);
        return new SkysailResponse<>();
    }

}
