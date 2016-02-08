package io.skysail.server.app.facebookClient;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class OAuthConfigsResource extends ListServerResource<io.skysail.server.app.facebookClient.OAuthConfig> {

    private FacebookClientApplication app;
    private OAuthConfigRepository repository;

    public OAuthConfigsResource() {
        super(OAuthConfigResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list OAuthConfigs");
    }

    public OAuthConfigsResource(Class<? extends OAuthConfigResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (FacebookClientApplication) getApplication();
        repository = (OAuthConfigRepository) app.getRepository(io.skysail.server.app.facebookClient.OAuthConfig.class);
    }

    /*@Override
    public Set<String> getRestrictedToMediaTypes() {
        return super.getRestrictedToMediaTypes("text/prs.skysail-uikit");
    }*/

    @Override
    public List<io.skysail.server.app.facebookClient.OAuthConfig> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostOAuthConfigResource.class,OAuthConfigsResource.class);
    }
}