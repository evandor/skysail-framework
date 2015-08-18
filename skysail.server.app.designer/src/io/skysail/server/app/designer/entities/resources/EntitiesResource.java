package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class EntitiesResource extends ListServerResource<Entity> {

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
        Application application = app.getRepository().getById(Application.class, id);
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("/applications/" + id, application.getName());
        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
    }

    @Override
    public List<Entity> getEntity() {
        String sql = "SELECT * from " + Entity.class.getSimpleName() + " WHERE #"+id+" IN in('entities')";
        return app.getRepository().findAll(sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostEntityResource.class);
    }

}
