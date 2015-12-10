package io.skysail.server.app.designer.entities.resources;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class EntitiesResource extends ListServerResource<DbEntity> {

    private DesignerApplication app;
    private String id;

    public EntitiesResource() {
        super(EntityResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Entities");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        /*DbApplication application = app.getRepository().getById(DbApplication.class, id);
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("/applications/" + id, application.getName());
        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);*/
    }

    @Override
    public List<DbEntity> getEntity() {
        String sql = "SELECT from " + DbClassName.of(DbEntity.class) + " WHERE #"+id+" IN in('entities')";
        return app.getRepository().findEntities(sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostEntityResource.class);
    }

}
