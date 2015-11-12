package io.skysail.server.app.dbviewer;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ConnectionResource extends EntityServerResource<Connection> {

    private DbviewerApplication app;
    private ConnectionRepo repository;

    protected void doInit() {
        super.doInit();
        app = (DbviewerApplication)getApplication();
        repository = (ConnectionRepo) app.getRepository(Connection.class);
    }

    public Connection getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutConnectionResource.class);
    }

    public SkysailResponse<Connection> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader();
    }
}
