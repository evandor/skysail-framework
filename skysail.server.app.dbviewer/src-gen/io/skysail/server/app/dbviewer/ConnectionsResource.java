package io.skysail.server.app.dbviewer;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ConnectionsResource extends ListServerResource<Connection> {

    private io.skysail.server.app.dbviewer.DbviewerApplication app;

    public ConnectionsResource() {
        super(ConnectionResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Connections");
    }

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.dbviewer.DbviewerApplication)getApplication();
    }

    @Override
    public List<Connection> getEntity() {
        return app.getConnectionRepository().find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostConnectionResource.class, ConnectionsResource.class);
    }
}
