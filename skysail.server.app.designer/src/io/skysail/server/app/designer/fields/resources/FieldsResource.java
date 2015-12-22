package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.restlet.resources.ListServerResource;

public class FieldsResource extends ListServerResource<DbEntityField> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    public FieldsResource() {
        super(FieldResource.class);
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
    }

    @Override
    public List<DbEntityField> getEntity() {
        DbEntity entity = app.getEntity(entityId);
        if (entity != null) {
            return entity.getFields();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostFieldResource.class, PostActionFieldResource.class);
    }
}
