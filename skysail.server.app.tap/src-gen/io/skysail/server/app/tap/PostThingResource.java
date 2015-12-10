package io.skysail.server.app.tap;

import javax.annotation.Generated;

//

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostThingResource extends PostEntityServerResource<Thing> {

	private TapApplication app;
    private ThingRepo repository;

	public PostThingResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Thing");
    }

    @Override
	protected void doInit() {
		app = (TapApplication)getApplication();
        repository = (ThingRepo) app.getRepository(Thing.class);
	}

	@Override
    public Thing createEntityTemplate() {
	    return new Thing();
    }

    @Override
    public void addEntity(Thing entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ThingsResource.class);
	}
}