package io.skysail.server.app.tap;

import javax.annotation.Generated;

//import io.skysail.server.app.tap.*;


import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostThingResource extends PostEntityServerResource<Thing> {

	private io.skysail.server.app.tap.TabApplication app;
    private ThingRepo repository;

	public PostThingResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Thing");
    }

    @Override
	protected void doInit() {
		app = (io.skysail.server.app.tap.TabApplication)getApplication();
        repository = (ThingRepo) app.getRepository(Thing.class);
	}

	@Override
    public Thing createEntityTemplate() {
	    return new Thing();
    }

    @Override
    public SkysailResponse<Thing> addEntity(Thing entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(ThingsResource.class);
	}
}