package io.skysail.server.app.designer.entities.resources;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostEntityResource extends PostEntityServerResource<DbEntity> {

    private DesignerApplication app;
    private Repository repo;

    public PostEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new DbEntity");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = app.getRepository(DbApplication.class);
    }

    @Override
    public DbEntity createEntityTemplate() {
        return new DbEntity();
    }

    @Override
    public void addEntity(DbEntity entity) {
        DbApplication dbApplication = (DbApplication) repo.findOne(getAttribute("id"));
        dbApplication.getEntities().add(entity);
        repo.update(dbApplication.getId(), dbApplication, "entities").toString();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesResource.class);
    }
}
