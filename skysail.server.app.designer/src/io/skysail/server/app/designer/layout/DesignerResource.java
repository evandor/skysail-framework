package io.skysail.server.app.designer.layout;

import java.util.List;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class DesignerResource extends ListServerResource<DbEntity> {
    
    private DesignerApplication app;
    private String id;
    private DbApplication dbApplication;

    public DesignerResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Designer");
        addToContext(ResourceContextId.LINK_GLYPH, "chevron-down");
       }

       @Override
       protected void doInit() {
           super.doInit();
           app = (DesignerApplication) getApplication();
           id = getAttribute("id");
           dbApplication = app.getRepository().getById(DbApplication.class, id);
           setUrlSubsitution("applications", id, dbApplication != null ? dbApplication.getName() : "unknown");
       }

       @Override
       public List<DbEntity> getEntity() {
           String sql = "SELECT from " + DbClassName.of(DbEntity.class) + " WHERE #" + id + " IN in('entities')";
           return app.getRepository().findEntities(sql);
       }

}
