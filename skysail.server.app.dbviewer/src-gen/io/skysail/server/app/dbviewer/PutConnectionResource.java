package io.skysail.server.app.dbviewer;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutConnectionResource extends PutEntityServerResource<Connection> {

    private io.skysail.server.app.dbviewer.DbviewerApplication app;
    private ConnectionRepo repository;

	protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.dbviewer.DbviewerApplication) getApplication();
        repository = (ConnectionRepo) app.getRepository(Connection.class);
    }

    public Connection getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Connection> updateEntity(Connection entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ConnectionsResource.class);
    }
}
