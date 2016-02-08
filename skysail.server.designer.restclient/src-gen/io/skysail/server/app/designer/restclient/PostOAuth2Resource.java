package io.skysail.server.app.designer.restclient;

import javax.annotation.Generated;

//

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostOAuth2Resource extends PostEntityServerResource<OAuth2> {

	private RestclientApplication app;
    private OAuth2Repo repository;

	public PostOAuth2Resource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new OAuth2");
    }

    @Override
	protected void doInit() {
		app = (RestclientApplication)getApplication();
        repository = (OAuth2Repo) app.getRepository(OAuth2.class);
	}

	@Override
    public OAuth2 createEntityTemplate() {
	    return new OAuth2();
    }

    @Override
    public void addEntity(OAuth2 entity) {
        String id = repository.save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(OAuth2sResource.class);
	}
}