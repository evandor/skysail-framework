package io.skysail.app.propman;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostRequestResource extends PostEntityServerResource<Request> {

	private PropManApplication app;

    public PostRequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Request");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PropManApplication) getApplication();
    }

    @Override
    public Request createEntityTemplate() {
        return new Request();
    }

    @Override
    public SkysailResponse<?> addEntity(Request entity) {
        Subject subject = SecurityUtils.getSubject();
        Campaign root = app.getRepository().getById(Campaign.class, getAttribute("id"));
        root.addRequest(entity);
        app.getRepository().update(getAttribute("id"), root);
        return new SkysailResponse<String>();

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RequestsResource.class);
    }
}