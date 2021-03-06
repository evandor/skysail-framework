package io.skysail.server.app.designer.restclient;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutOAuth2Resource extends PutEntityServerResource<OAuth2> {

    protected RestclientApplication app;
    protected OAuth2Repo repository;

	protected void doInit() {
        super.doInit();
        app = (RestclientApplication) getApplication();
        repository = (OAuth2Repo) app.getRepository(OAuth2.class);
    }

    public OAuth2 getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public void updateEntity(OAuth2 entity) {
        repository.update(entity, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(OAuth2sResource.class);
    }
}
