package io.skysail.server.app.tap;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutPlaceResource extends PutEntityServerResource<Place> {

    private io.skysail.server.app.tap.TabApplication app;

	protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.tap.TabApplication) getApplication();
    }

    public Place getEntity() {
        return app.getPlaceRepository().findOne(getAttribute("id"));
    }

    public SkysailResponse<Place> updateEntity(Place entity) {
        app.getPlaceRepository().update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PlacesResource.class);
    }
}
