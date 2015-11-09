package io.skysail.server.app.designer.restclient;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutClientApplicationResource extends PutEntityServerResource<ClientApplication> {

    private io.skysail.server.app.designer.restclient.RestclientApplication app;
    private ClientApplicationRepo repository;

	protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.designer.restclient.RestclientApplication) getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
    }

    public ClientApplication getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<ClientApplication> updateEntity(ClientApplication entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ClientApplicationsResource.class);
    }
}
