package io.skysail.server.app.facebookClient;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostOAuthConfigResource extends PostEntityServerResource<io.skysail.server.app.facebookClient.OAuthConfig> {

	protected FacebookClientApplication app;

    public PostOAuthConfigResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (FacebookClientApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.facebookClient.OAuthConfig createEntityTemplate() {
        return new OAuthConfig();
    }

    @Override
    public void addEntity(io.skysail.server.app.facebookClient.OAuthConfig entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.facebookClient.OAuthConfig.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(OAuthConfigsResource.class);
    }
}