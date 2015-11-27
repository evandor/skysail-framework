package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostEntityResource extends PostEntityServerResource<Entity> {

    private DesignerApplication app;
    private String id;

    public PostEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Entity");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
    }

    @Override
    public Entity createEntityTemplate() {
        return new Entity();
    }

    @Override
    public SkysailResponse<Entity> addEntity(Entity entity) {

        Application theApplication = (Application) app.getRepository().findOne(getAttribute("id"));
//        ORecordId added = (ORecordId) DesignerRepository.add(entity);
//
//        Application application = app.getRepository().getById(Application.class, id);
//        application.getEntities().add(added.getIdentity().toString());
        theApplication.getEntities().add(entity);
        app.getRepository().update(theApplication, "entities");
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> l.substitute("id", id);
    }
}
