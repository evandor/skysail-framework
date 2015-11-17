package io.skysail.server.app.designer.restclient;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class OAuth2Resource extends EntityServerResource<OAuth2> {

    private RestclientApplication app;
    private OAuth2Repo repository;

    protected void doInit() {
        super.doInit();
        app = (RestclientApplication)getApplication();
        repository = (OAuth2Repo) app.getRepository(OAuth2.class);
    }

    public OAuth2 getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutOAuth2Resource.class);
    }

    public SkysailResponse<OAuth2> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}
