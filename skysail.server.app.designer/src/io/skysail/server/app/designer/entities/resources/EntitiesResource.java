package io.skysail.server.app.designer.entities.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.layout.DesignerResource;
import io.skysail.server.app.designer.valueobjects.resources.PostValueObjectsResource;
import io.skysail.server.app.designer.valueobjects.resources.ValueObjectsResource;
import io.skysail.server.db.DbClassName;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.ListServerResource;

public class EntitiesResource extends ListServerResource<DbEntity> {

    private DesignerApplication app;
    private String id;
    private DbApplication dbApplication;

    public EntitiesResource() {
        super(EntityResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Entities");
        addToContext(ResourceContextId.LINK_GLYPH, "chevron-down");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        dbApplication = app.getRepository().getById(DbApplication.class, id);
        setUrlSubsitution("applications", id, dbApplication != null ? dbApplication.getName() : "unknown");
    }

    @Override
    public List<DbEntity> getEntity() {
        String sql = "SELECT from " + DbClassName.of(DbEntity.class) + " WHERE #" + id + " IN in('entities')";
        return app.getRepository().findEntities(sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostEntityResource.class, DesignerResource.class, ValueObjectsResource.class, PostValueObjectsResource.class);
    }

    @Override
    public List<TreeStructure> getTreeRepresentation() {
        return app.getTreeRepresentation(getAttribute("id"));
    }

}
