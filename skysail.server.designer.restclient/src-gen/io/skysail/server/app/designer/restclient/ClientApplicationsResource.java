package io.skysail.server.app.designer.restclient;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ClientApplicationsResource extends ListServerResource<ClientApplication> {

    private io.skysail.server.app.designer.restclient.RestclientApplication app;
    private ClientApplicationRepo repository;

    public ClientApplicationsResource() {
        super(ClientApplicationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of ClientApplications");
    }

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.designer.restclient.RestclientApplication)getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
    }

    @Override
    public List<ClientApplication> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostClientApplicationResource.class, ClientApplicationsResource.class);
    }
}
