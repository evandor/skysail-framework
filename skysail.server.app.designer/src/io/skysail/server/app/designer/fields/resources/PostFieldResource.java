package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostFieldResource extends PostEntityServerResource<EntityField> {

    private DesignerApplication app;

    public PostFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new EntityField");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
    }

    @Override
    public EntityField createEntityTemplate() {
        return new EntityField();
    }

    @Override
    public SkysailResponse<EntityField> addEntity(EntityField field) {
        Entity theEntity = app.getRepository().getById(Entity.class, getAttribute(DesignerApplication.ENTITY_ID));
        theEntity.getFields().add(field);
        app.getRepository().update(theEntity, "fields");
        return new SkysailResponse<>();
    }

}
