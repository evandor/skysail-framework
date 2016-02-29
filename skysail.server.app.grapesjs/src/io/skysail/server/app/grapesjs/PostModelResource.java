package io.skysail.server.app.grapesjs;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.grapesjs.GrapesJsApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostModelResource extends PostEntityServerResource<io.skysail.server.app.grapesjs.Model> {

	protected GrapesJsApplication app;

    public PostModelResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (GrapesJsApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.grapesjs.Model createEntityTemplate() {
        return new Model();
    }

    @Override
    public void addEntity(io.skysail.server.app.grapesjs.Model entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.grapesjs.Model.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ModelsResource.class);
    }
}