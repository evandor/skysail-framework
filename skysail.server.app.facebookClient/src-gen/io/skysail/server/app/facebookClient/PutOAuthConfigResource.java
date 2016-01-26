package io.skysail.server.app.facebookClient;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutOAuthConfigResource extends PutEntityServerResource<io.skysail.server.app.facebookClient.OAuthConfig> {


    protected String id;
    protected FacebookClientApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (FacebookClientApplication)getApplication();
    }

    @Override
    public void updateEntity(OAuthConfig  entity) {
        io.skysail.server.app.facebookClient.OAuthConfig original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.facebookClient.OAuthConfig.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.facebookClient.OAuthConfig getEntity() {
        return (io.skysail.server.app.facebookClient.OAuthConfig)app.getRepository(io.skysail.server.app.facebookClient.OAuthConfig.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(OAuthConfigsResource.class);
    }
}