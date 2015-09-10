package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutFieldResource extends PutEntityServerResource<EntityField> {

    private DesignerApplication app;
    private String appId;
    private String entityId;
    private String fieldId;

    protected void doInit() {
        super.doInit();
        appId = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
        fieldId = getAttribute(DesignerApplication.FIELD_ID);
        app = (DesignerApplication) getApplication();
    }

    public EntityField getEntity() {
        return app.getEntityField(appId, entityId, fieldId);
    }

    public SkysailResponse<EntityField> updateEntity(EntityField entity) {
        app.getRepository().update(entity);
        return new SkysailResponse<>();
    }

}
