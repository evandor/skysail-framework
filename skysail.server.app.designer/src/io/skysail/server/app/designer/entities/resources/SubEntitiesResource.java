package io.skysail.server.app.designer.entities.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

public class SubEntitiesResource extends ListServerResource<Entity> {

    private DesignerApplication app;
    private String id;
    private String entityId;

    public SubEntitiesResource() {
        super(SubEntityResource.class);
    }
    
    @Override
    protected void doInit() throws ResourceException {
        app = (DesignerApplication) getApplication();
        id = getAttribute("id");
        entityId = getAttribute(DesignerApplication.ENTITY_ID);
    }
    
    @Override
    public List<Entity> getEntity() {
        Application application = app.getRepository().getById(Application.class, id);
        Optional<Entity> parentEntity = app.getEntityFromApplication(application, entityId);
        if(parentEntity.isPresent()) {
            return parentEntity.get().getSubEntities();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostEntityResource.class);
    }
    
    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> l.substitute("id", id).substitute(DesignerApplication.ENTITY_ID, entityId);
    }

}
