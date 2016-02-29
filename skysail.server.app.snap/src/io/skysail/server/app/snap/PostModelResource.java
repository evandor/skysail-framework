package io.skysail.server.app.snap;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.snap.SnapApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostModelResource extends PostEntityServerResource<io.skysail.server.app.snap.Model> {

	protected SnapApplication app;

    public PostModelResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (SnapApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.snap.Model createEntityTemplate() {
        return new Model();
    }

    @Override
    public void addEntity(io.skysail.server.app.snap.Model entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.snap.Model.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ModelsResource.class);
    }
}