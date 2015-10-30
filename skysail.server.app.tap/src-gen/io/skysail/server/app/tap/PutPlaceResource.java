package io.skysail.server.app.tap;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutPlaceResource extends PutEntityServerResource<Place> {

    private io.skysail.server.app.tap.TabApplication app;
    private PlaceRepo repository;

	protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.tap.TabApplication) getApplication();
        repository = (PlaceRepo) app.getRepository(Place.class);
    }

    public Place getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Place> updateEntity(Place entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PlacesResource.class);
    }
}
