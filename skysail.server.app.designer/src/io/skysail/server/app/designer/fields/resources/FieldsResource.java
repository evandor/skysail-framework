package io.skysail.server.app.designer.fields.resources;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class FieldsResource extends ListServerResource<EntityField> {

    private DesignerApplication app;
    private String id;
    private String entityName;

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityName = getAttribute("entityName");
    }

    @Override
    public List<EntityField> getEntity() {
        Application application = app.getApplication(id);
        Entity entity = app.getEntity(application, entityName);
        if (entity != null) {
            return entity.getFields();
        }
        return Collections.emptyList();
    }

}
