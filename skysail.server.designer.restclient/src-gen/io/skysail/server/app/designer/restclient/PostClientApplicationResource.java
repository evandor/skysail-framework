package io.skysail.server.app.designer.restclient;

import javax.annotation.Generated;

//

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostClientApplicationResource extends PostEntityServerResource<ClientApplication> {

	private RestclientApplication app;
    private ClientApplicationRepo repository;

	public PostClientApplicationResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new ClientApplication");
    }

    @Override
	protected void doInit() {
		app = (RestclientApplication)getApplication();
        repository = (ClientApplicationRepo) app.getRepository(ClientApplication.class);
	}

	@Override
    public ClientApplication createEntityTemplate() {
	    return new ClientApplication();
    }

    @Override
    public SkysailResponse<ClientApplication> addEntity(ClientApplication entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ClientApplicationsResource.class);
	}
}