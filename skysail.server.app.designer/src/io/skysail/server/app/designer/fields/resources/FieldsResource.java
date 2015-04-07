package io.skysail.server.app.designer.fields.resources;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.Collections;
import java.util.List;

public class FieldsResource extends ListServerResource<EntityField> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
    }

    @Override
    public List<EntityField> getEntity() {
        Application application = app.getApplication(id);
        Entity entity = app.getEntity(application, entityId);
        if (entity != null) {
            return entity.getFields();
        }
        return Collections.emptyList();
    }

}
