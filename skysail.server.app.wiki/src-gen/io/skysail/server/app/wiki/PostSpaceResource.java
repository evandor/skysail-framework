package io.skysail.server.app.wiki;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostSpaceResource extends PostEntityServerResource<io.skysail.server.app.wiki.Space> {

	protected WikiApplication app;

    public PostSpaceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (WikiApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.wiki.Space createEntityTemplate() {
        return new Space();
    }

    @Override
    public void addEntity(io.skysail.server.app.wiki.Space entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.wiki.Space.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }
}