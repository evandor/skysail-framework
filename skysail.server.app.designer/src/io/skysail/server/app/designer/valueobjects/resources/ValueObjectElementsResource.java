package io.skysail.server.app.designer.valueobjects.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.layout.DesignerResource;
import io.skysail.server.app.designer.valueobjects.DbValueObjectElement;
import io.skysail.server.db.DbClassName;
import io.skysail.server.model.TreeStructure;
import io.skysail.server.restlet.resources.ListServerResource;

public class ValueObjectElementsResource extends ListServerResource<DbValueObjectElement> {

    private DesignerApplication app;
    private String id;
    private DbApplication dbApplication;

    public ValueObjectElementsResource() {
        super(ValueObjectElementResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list ValueObjects");
        addToContext(ResourceContextId.LINK_GLYPH, "chevron-down");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        //dbApplication = app.getRepository().getById(DbApplication.class, id);
        //setUrlSubsitution("applications", id, dbApplication != null ? dbApplication.getName() : "unknown");
    }

    @Override
    public List<DbValueObjectElement> getEntity() {
        String sql = "SELECT from " + DbClassName.of(DbValueObjectElement.class) + " WHERE #" + id + " IN in('elements')";
        return app.getRepository().findValueObjectElements(sql);
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
