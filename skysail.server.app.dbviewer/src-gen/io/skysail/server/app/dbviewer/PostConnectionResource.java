package io.skysail.server.app.dbviewer;

import javax.annotation.Generated;

//import io.skysail.server.app.dbviewer.*;


import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostConnectionResource extends PostEntityServerResource<Connection> {

	private io.skysail.server.app.dbviewer.DbviewerApplication app;
    private ConnectionRepo repository;

	public PostConnectionResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Connection");
    }

    @Override
	protected void doInit() {
		app = (io.skysail.server.app.dbviewer.DbviewerApplication)getApplication();
        repository = (ConnectionRepo) app.getRepository(Connection.class);
	}

	@Override
    public Connection createEntityTemplate() {
	    return new Connection();
    }

    @Override
    public SkysailResponse<Connection> addEntity(Connection entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ConnectionsResource.class);
	}
}