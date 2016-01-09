package io.skysail.server.app.designer.relations.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.relations.DbRelation;
import io.skysail.server.restlet.resources.EntityServerResource;

public class RelationResource extends EntityServerResource<DbRelation> {

    private DesignerApplication app;

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication)getApplication();
    }
    
    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public DbRelation getEntity() {
        return null;
    }
    
}
