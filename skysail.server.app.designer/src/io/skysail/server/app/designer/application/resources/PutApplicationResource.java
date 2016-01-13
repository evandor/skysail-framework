package io.skysail.server.app.designer.application.resources;

import java.util.List;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.forms.Tab;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutApplicationResource extends PutEntityServerResource<DbApplication> {

    private DesignerApplication app;
    private String id;

    protected void doInit() {
        super.doInit();
        id = getAttribute("id");
        app = (DesignerApplication) getApplication();
    }

    public DbApplication getEntity() {
        return app.getRepository().getById(DbApplication.class, id);
    }

    public void updateEntity(DbApplication entity) {
        app.invalidateMenuCache();
        app.getRepository().update(entity, ((DesignerApplication)getApplication()).getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }
    
    @Override
    public List<Tab> getTabs() {
        return super.getTabs(new Tab("newApp","new application",1), new Tab("details","details",2));
    }

}
