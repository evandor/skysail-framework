package io.skysail.server.app.designer.relations.resources;

import java.util.List;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
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
        switch (entity.getRelationType()) {
        case "ONE_TO_MANY":
            List<DbEntity> oneToManyRelations = dbEntity.getOneToManyRelations();
            String target = entity.getTarget();
            DbEntity targetEntity = app.getRepository().findEntity(target);
            oneToManyRelations.add(targetEntity);
            break;

        default:
            throw new IllegalStateException();
        }
        app.getRepository().update(dbEntity, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }
}
