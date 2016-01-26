package io.skysail.server.app.facebookClient;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class OAuthConfigResource extends EntityServerResource<io.skysail.server.app.facebookClient.OAuthConfig> {

    private String id;
    private FacebookClientApplication app;
    private OAuthConfigRepository repository;

    public OAuthConfigResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (FacebookClientApplication) getApplication();
        repository = (OAuthConfigRepository) app.getRepository(io.skysail.server.app.facebookClient.OAuthConfig.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.facebookClient.OAuthConfig getEntity() {
        return (io.skysail.server.app.facebookClient.OAuthConfig)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutOAuthConfigResource.class);
    }

}