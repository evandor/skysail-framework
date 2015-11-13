package io.skysail.server.app.designer.restclient;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ClientApplicationResource extends EntityServerResource<ClientApplication> {

    private RestclientApplication app;
    private ClientApplicationRepo repository;

    protected void doInit() {
        super.doInit();
        app = (RestclientApplication)getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
    }

    public ClientApplication getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutClientApplicationResource.class);//, ExecutionResource.class);
    }

    public SkysailResponse<ClientApplication> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}
