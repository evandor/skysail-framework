package io.skysail.server.app.designer.entities.resources;

import java.util.Optional;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostEntityResource extends PostEntityServerResource<DbEntity> {

    private DesignerApplication app;
    private DesignerRepository repo;

    public PostEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new DbEntity");
    }

    @Override
    protected void doInit() {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
    }

    @Override
    public DbEntity createEntityTemplate() {
        return new DbEntity();
    }

    @Override
    public void addEntity(DbEntity entity) {
        DbApplication dbApplication = (DbApplication) repo.findOne(getAttribute("id"));
       // entity.setApplication(dbApplication);
        
        Optional<DbEntity> existingEntityWithSameName = dbApplication.getEntities().stream().filter(e -> e.getName().equals(entity.getName())).findFirst();
        if (existingEntityWithSameName.isPresent()) {
            throw new IllegalStateException("entity with same name already exists");
        }
        
        
        dbApplication.getEntities().add(entity);
        //repo.update(dbApplication.getId(), dbApplication, "entities").toString();
        repo.update(dbApplication, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesResource.class);
    }
}
