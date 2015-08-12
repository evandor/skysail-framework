package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import com.orientechnologies.orient.core.id.ORecordId;

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
    public SkysailResponse<?> addEntity(Entity entity) {

        ORecordId added = (ORecordId) DesignerRepository.add(entity);

        Application application = app.getRepository().getById(Application.class, id);
        application.getEntities().add(added.getIdentity().toString());
        app.getRepository().update(application, "entities");
        return new SkysailResponse<String>();
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
