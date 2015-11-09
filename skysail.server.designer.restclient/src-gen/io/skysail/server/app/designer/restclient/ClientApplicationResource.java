package io.skysail.server.app.designer.restclient;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;


import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ClientApplicationResource extends EntityServerResource<ClientApplication> {

    private io.skysail.server.app.designer.restclient.RestclientApplication app;
    private ClientApplicationRepo repository;

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.designer.restclient.RestclientApplication)getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
    }

    public ClientApplication getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutClientApplicationResource.class);
    }

    public SkysailResponse<ClientApplication> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader(PutClientApplicationResource.class);
    }
}
