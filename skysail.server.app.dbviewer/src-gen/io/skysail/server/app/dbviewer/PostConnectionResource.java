package io.skysail.server.app.dbviewer;

import javax.annotation.Generated;

//

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostConnectionResource extends PostEntityServerResource<Connection> {

	private DbviewerApplication app;
    private ConnectionRepo repository;

	public PostConnectionResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Connection");
    }

    @Override
	protected void doInit() {
		app = (DbviewerApplication)getApplication();
        repository = (ConnectionRepo) app.getRepository(Connection.class);
	}

	@Override
    public Connection createEntityTemplate() {
	    return new Connection();
    }

    @Override
    public void addEntity(Connection entity) {
        String id = repository.save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ConnectionsResource.class);
	}
}