package io.skysail.server.app.designer.restclient;

import org.restlet.representation.Representation;
import org.restlet.resource.*;

public class ExecutionResource extends ServerResource {

    private RestclientApplication app;
    private ClientApplicationRepo repository;

    public ExecutionResource() {
        //addToContext(ResourceContextId.LINK_TITLE, "execute API");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (RestclientApplication)getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
    }

    @Get
    public Representation getEntity() {
        ClientApplication ca = repository.findOne(getAttribute("id"));
        String uri = ca.getUrl().replace(":@", ca.getUsername() + ":" + ca.getPassword() + "@");
        ClientResource cr = new ClientResource(uri);
        return cr.get();
    }


}
