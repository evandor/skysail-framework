package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.fields.ActionEntityField;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostActionFieldResource extends PostEntityServerResource<ActionEntityField> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    public PostActionFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Action Field");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
    }

    @Override
    public ActionEntityField createEntityTemplate() {
        return new ActionEntityField();
    }

    @Override
    public SkysailResponse<ActionEntityField> addEntity(ActionEntityField field) {
        DbEntity entity = app.getRepository().getById(DbEntity.class, entityId);
        entity.getActionFields().add(field);
        app.getRepository().update(entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesResource.class);
    }


}
