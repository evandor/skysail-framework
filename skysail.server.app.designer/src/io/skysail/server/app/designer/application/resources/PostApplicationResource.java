package io.skysail.server.app.designer.application.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostApplicationResource extends PostEntityServerResource<DbApplication> {

    private DesignerApplication app;

    public PostApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new DbApplication");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DesignerApplication) getApplication();
    }

    @Override
    public DbApplication createEntityTemplate() {
        return new DbApplication();
    }

    @Override
    public SkysailResponse<DbApplication> addEntity(DbApplication entity) {
        app.invalidateMenuCache();
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        String id = DesignerRepository.add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ApplicationsResource.class);
    }
}
