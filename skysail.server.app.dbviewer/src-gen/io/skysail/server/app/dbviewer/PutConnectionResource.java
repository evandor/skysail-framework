package io.skysail.server.app.dbviewer;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutConnectionResource extends PutEntityServerResource<Connection> {

    protected DbviewerApplication app;
    protected ConnectionRepo repository;

	protected void doInit() {
        super.doInit();
        app = (DbviewerApplication) getApplication();
        repository = (ConnectionRepo) app.getRepository(Connection.class);
    }

    public Connection getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public void updateEntity(Connection entity) {
        repository.update(entity, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ConnectionsResource.class);
    }
}
