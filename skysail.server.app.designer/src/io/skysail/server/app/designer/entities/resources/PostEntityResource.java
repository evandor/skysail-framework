package io.skysail.server.app.designer.entities.resources;

import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.api.repos.Repository;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostEntityResource extends PostEntityServerResource<DbEntity> {

    private DesignerApplication app;
    private String id;
    private Repository repo;

    public PostEntityResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new DbEntity");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
        repo = app.getRepository(DbApplication.class);
        id = getAttribute("id");
    }

    @Override
    public DbEntity createEntityTemplate() {
        return new DbEntity();
    }

    @Override
    public SkysailResponse<DbEntity> addEntity(DbEntity entity) {
        DbApplication dbApplication = (DbApplication) repo.findOne(getAttribute("id"));
        dbApplication.getEntities().add(entity);
        repo.update(dbApplication.getId(), dbApplication, "entities").toString();
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
