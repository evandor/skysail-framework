package io.skysail.server.app.designer.fields.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostFieldResource extends PostEntityServerResource<EntityField> {

    private DesignerApplication app;
    private String id;
    private String entityName;

    public PostFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new EntityField");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityName = getAttribute("entityName");
    }

    @Override
    public EntityField createEntityTemplate() {
        return new EntityField();
    }

    @Override
    public SkysailResponse<?> addEntity(EntityField field) {
        Application application = app.getApplication(id);
        Entity entity = app.getEntity(application, entityName);
        entity.getFields().add(field);
        app.getRepository().update(application);
        return new SkysailResponse<String>();
    }

}
