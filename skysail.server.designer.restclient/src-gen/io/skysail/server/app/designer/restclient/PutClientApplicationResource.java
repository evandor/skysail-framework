package io.skysail.server.app.designer.restclient;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutClientApplicationResource extends PutEntityServerResource<ClientApplication> {

    protected RestclientApplication app;
    protected ClientApplicationRepo repository;

	protected void doInit() {
        super.doInit();
        app = (RestclientApplication) getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
    }

    public ClientApplication getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public void updateEntity(ClientApplication entity) {
        repository.update(entity, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ClientApplicationsResource.class);
    }
}
