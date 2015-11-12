package io.skysail.server.app.tap;

import javax.annotation.Generated;

//

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostPlaceResource extends PostEntityServerResource<Place> {

	private TabApplication app;
    private PlaceRepo repository;

	public PostPlaceResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Place");
    }

    @Override
	protected void doInit() {
		app = (TabApplication)getApplication();
        repository = (PlaceRepo) app.getRepository(Place.class);
	}

	@Override
    public Place createEntityTemplate() {
	    return new Place();
    }

    @Override
    public SkysailResponse<Place> addEntity(Place entity) {
        String id = repository.save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>(entity);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(PlacesResource.class);
	}
}