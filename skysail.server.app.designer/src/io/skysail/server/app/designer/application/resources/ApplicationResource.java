package io.skysail.server.app.designer.application.resources;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.codegen.PostCompilationResource;
import io.skysail.server.app.designer.entities.resources.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.*;

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
    public String getId() {
        return id;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
       // app.invalidateMenuCache();
        repo.delete(DbApplication.class, id);
        return new SkysailResponse<>();
    }

    @Override
    public DbApplication getEntity() {
        return repo.getById(DbApplication.class, id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutApplicationResource.class, ApplicationResource.class, PostEntityResource.class,
                EntitiesResource.class, PostCompilationResource.class);
    }

//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> {
//            if (id != null) {
//                l.substitute("id", id);
//            }
//        };
//    }

    @Override
    public String redirectTo(Class<? extends SkysailServerResource<?>> cls) {
        return super.redirectTo(ApplicationsResource.class);
    }

}
