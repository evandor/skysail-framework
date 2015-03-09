package io.skysail.server.app.designer.entities;

import io.skysail.server.app.designer.DesignerApplication;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class EntitiesResource extends ListServerResource<Entity> {

    private DesignerApplication app;

    public EntitiesResource() {
        addToContext(ResourceContextId.LINK_TITLE, "list Entities");
    }

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
    }

    @Override
    public List<Entity> getEntity() {
        return app.getRepository().findAll(Entity.class);
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostEntityResource.class);
    }

}
