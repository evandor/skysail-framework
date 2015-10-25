package io.skysail.server.app.tap;

import javax.annotation.Generated;

//import io.skysail.server.app.tap.*;


import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostPlaceResource extends PostEntityServerResource<Place> {

	private io.skysail.server.app.tap.TabApplication app;

	public PostPlaceResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Place");
    }

    @Override
	protected void doInit() {
		app = (io.skysail.server.app.tap.TabApplication)getApplication();
	}

	@Override
    public Place createEntityTemplate() {
	    return new Place();
    }

    @Override
    public SkysailResponse<Place> addEntity(Place entity) {
        String id = app.getPlaceRepository().save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(PlacesResource.class);
	}
}