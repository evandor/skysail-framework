package io.skysail.server.app.designer.fields.resources;

import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class FieldsResource extends ListServerResource<EntityField> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        
        Application application = app.getRepository().getById(Application.class, id);
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("/applications/" + id, application.getName());
        Entity entity = app.getRepository().getById(Entity.class, entityId);
        substitutions.put("/entities/" + entityId, entity.getName());
        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
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
