package io.skysail.server.app.designer.fields.resources;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostFieldResource extends PostEntityServerResource<DbEntityField> {

    private DesignerApplication app;
    private DesignerRepository repo;

    public PostFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new DbEntityField");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
    }

    @Override
    public DbEntityField createEntityTemplate() {
        return new DbEntityField();
    }

    @Override
    public void addEntity(DbEntityField field) {
        DbEntity theEntity = repo.getById(DbEntity.class, getAttribute(DesignerApplication.ENTITY_ID));
        theEntity.getFields().add(field);
        app.getRepository().update(theEntity, "fields");
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }

}
