package io.skysail.server.app.designer.valueobjects.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.valueobjects.ValueObject;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class ValueObjectsResource extends ListServerResource<ValueObject> {

    private DesignerApplication app;
    private String id;
    private DbApplication dbApplication;

    public ValueObjectsResource() {
        super(ValueObjectResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list ValueObjects");
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
    public List<ValueObject> getEntity() {
        String sql = "SELECT from " + DbClassName.of(ValueObject.class) + " WHERE #" + id + " IN in('valueobjects')";
        return app.getRepository().findValueObjects(sql);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(ApplicationsResource.class);
    }

}
