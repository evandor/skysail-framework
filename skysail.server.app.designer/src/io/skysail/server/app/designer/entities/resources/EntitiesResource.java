package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;

import java.util.List;
import java.util.function.Consumer;

import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class EntitiesResource extends ListServerResource<Entity> {

    private DesignerApplication app;
    private String id;

    public EntitiesResource() {
        // super(FieldsResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Entities");
    }

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
    }

    @Override
    public List<Entity> getEntity() {
        Application application = app.getRepository().getById(Application.class, id);
        return application.getEntities();
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PostEntityResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            if (id != null) {
                l.substitute("id", id);
            }
        };
    }

}
