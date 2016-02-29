package io.skysail.server.app.designer.application.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.resources.PostCompilationResource;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class ApplicationResource extends EntityServerResource<DbApplication> {

    private String id;
    private DesignerApplication app;
    private DesignerRepository repo;

    public ApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "show");
    }

    @Override
    protected void doInit() {
        super.doInit();
        id = getAttribute("id");
        app = (DesignerApplication) getApplication();
        repo = app.getRepository();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        repo.delete(DbApplication.class, id);
        return new SkysailResponse<>();
    }

    @Override
    public DbApplication getEntity() {
        return repo.getById(DbApplication.class, id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutApplicationResource.class,
                EntitiesResource.class, PostCompilationResource.class);
    }

    @Override
    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        return super.redirectTo(ApplicationsResource.class);
    }

}
