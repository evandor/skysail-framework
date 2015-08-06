package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class FieldResource extends EntityServerResource<EntityField> {

    private String appId;
    private String entityId;
    private String fieldId;
    private DesignerApplication app;

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

    public List<Link> getLinks() {
        return super.getLinks(FieldsResource.class, PutFieldResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
