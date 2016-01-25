package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.resources.editors.PostTrixeditorFieldResource;
import io.skysail.server.app.designer.fields.resources.text.PostTextFieldResource;
import io.skysail.server.app.designer.fields.resources.textarea.PostTextareaFieldResource;
import io.skysail.server.app.designer.fields.resources.url.PostUrlFieldResource;
import io.skysail.server.restlet.resources.*;

public class FieldsResource extends ListServerResource<DbEntityField> {

    private DesignerApplication app;
    private String entityId;

    public FieldsResource() {
        super(FieldResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list fields");
        addToContext(ResourceContextId.LINK_GLYPH, "chevron-down");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
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
        return super.getLinks(PostTextFieldResource.class, PostTextareaFieldResource.class, PostTrixeditorFieldResource.class, PostUrlFieldResource.class);
    }
    
    public List<TreeRepresentation> getTreeRepresentation() {
        DbApplication dbApplication = app.getRepository().getById(DbApplication.class, getAttribute("id"));
        if (dbApplication != null) {
            return Arrays.asList(new TreeRepresentation(dbApplication,"", "leaf"));
        }
        return Collections.emptyList();
    }

}
