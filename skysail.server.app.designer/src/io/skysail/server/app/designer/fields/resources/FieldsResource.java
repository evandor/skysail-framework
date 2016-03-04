package io.skysail.server.app.designer.fields.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.ListServerResource;

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
        return super.getLinks(new ArrayList<>(PostFieldResource.getExtendingClasses()));
    }
    
    public List<TreeStructure> getTreeRepresentation() {
        DbApplication dbApplication = app.getRepository().getById(DbApplication.class, getAttribute("id"));
        if (dbApplication != null) {
            return Arrays.asList(new TreeStructure(dbApplication,null,"", "leaf"));
        }
        return Collections.emptyList();
    }

}
