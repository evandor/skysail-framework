package io.skysail.server.app.designer.relations.resources;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.relations.DbRelation;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostRelationResource extends PostEntityServerResource<DbRelation> {

    private DesignerApplication app;

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication)getApplication();
    }

    @Override
    public DbRelation createEntityTemplate() {
        return new DbRelation();
    }
    
    @Override
    public void addEntity(DbRelation entity) {
        DbEntity dbEntity = app.getRepository().findEntity(getAttribute("eid"));
        dbEntity.getRelations().add(entity);
        app.getRepository().update(dbEntity, app.getApplicationModel());
    }

}
