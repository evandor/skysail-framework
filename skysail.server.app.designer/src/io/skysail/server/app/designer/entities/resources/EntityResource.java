package io.skysail.server.app.designer.entities.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.resources.FieldsResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.EntityServerResource;

public class EntityResource extends EntityServerResource<DbEntity> {

    private String appId;
    private String entityId;
    private DesignerApplication app;
    private DesignerRepository repo;

    @Override
    protected void doInit() {
        super.doInit();
        appId = getAttribute("id");
        entityId = getAttribute("eid");
        app = (DesignerApplication) getApplication();
        repo = (DesignerRepository) app.getRepository(DbApplication.class);
    }

    @Override
    public DbEntity getEntity() {
        return repo.findEntity(entityId);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutEntityResource.class, FieldsResource.class, PostSubEntityResource.class, SubEntitiesResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // app.invalidateMenuCache();
        DbApplication dbApplication = (DbApplication) repo.findOne(appId);
        DbEntity entityToDelete = getEntity();
        dbApplication.getEntities().remove(entityToDelete);
        repo.update(dbApplication.getId(), dbApplication, "entities").toString();
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        // TODO Auto-generated method stub
        return super.redirectTo(ApplicationsResource.class);
    }

}
