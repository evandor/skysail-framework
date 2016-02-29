package io.skysail.server.app.dbviewer;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ConnectionsResource extends ListServerResource<Connection> {

    private DbviewerApplication app;
    private ConnectionRepo repository;

    public ConnectionsResource() {
        super(ConnectionResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Connections");
    }

    protected void doInit() {
        super.doInit();
        app = (DbviewerApplication)getApplication();
        repository = (ConnectionRepo) app.getRepository(Connection.class);
    }

    @Override
    public List<Connection> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
