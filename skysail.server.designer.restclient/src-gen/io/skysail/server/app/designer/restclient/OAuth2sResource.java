package io.skysail.server.app.designer.restclient;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class OAuth2sResource extends ListServerResource<OAuth2> {

    private RestclientApplication app;
    private OAuth2Repo repository;

    public OAuth2sResource() {
        super(OAuth2Resource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of OAuth2s");
    }

    protected void doInit() {
        super.doInit();
        app = (RestclientApplication)getApplication();
        repository = (OAuth2Repo) app.getRepository(OAuth2.class);
    }

    @Override
    public List<OAuth2> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
