package io.skysail.server.app.designer.entities.resources;

import java.util.List;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.restlet.resources.*;

public class PutEntityResource extends PutEntityServerResource<DbEntity> {

    private DesignerApplication app;
    private String entityId;

    @Override
    protected void doInit() {
        super.doInit();
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        app = (DesignerApplication) getApplication();
    }

    @Override
    public DbEntity getEntity() {
        return app.getEntity(entityId);
    }

    @Override
    public void updateEntity(DbEntity entity) {
        app.getRepository().update(entity);
    }
    
    @Override
    public List<TreeRepresentation> getTreeRepresentation() {
        return app.getTreeRepresentation(getAttribute("id"));
    }


}
