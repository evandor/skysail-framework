package io.skysail.server.app.dbviewer;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.dbviewer.dbclasses.DbClassesResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ConnectionResource extends EntityServerResource<Connection> {

    private io.skysail.server.app.dbviewer.DbviewerApplication app;

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.dbviewer.DbviewerApplication)getApplication();
    }

    public Connection getEntity() {
        return app.getConnectionRepository().findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutConnectionResource.class, DbClassesResource.class);
    }

    public SkysailResponse<Connection> eraseEntity() {
        app.getConnectionRepository().delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader(PutConnectionResource.class);
    }
}
