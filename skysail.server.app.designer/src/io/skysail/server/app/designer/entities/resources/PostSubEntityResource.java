package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Slf4j
public class PostSubEntityResource extends PostEntityServerResource<Entity> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    public PostSubEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create 1:n Relation");
        removeFromContext(ResourceContextId.LINK_GLYPH);
    }
    
    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
    }
    
    @Override
    public Entity createEntityTemplate() {
        return new Entity();
    }

    @Override
    public SkysailResponse<?> addEntity(Entity entity) {
        Application application = app.getRepository().getById(Application.class, id);
        Optional<Entity> parentEntity = app.getEntityFromApplication(application, entityId);
        if(parentEntity.isPresent()) {
            parentEntity.get().getSubEntities().add(entity);
            app.getRepository().update(parentEntity.get());
        } else {
            log.warn("could not find entity with id '{}'", entity);
        }
        return new SkysailResponse<String>();
    }

}
