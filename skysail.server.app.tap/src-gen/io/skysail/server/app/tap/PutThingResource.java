package io.skysail.server.app.tap;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutThingResource extends PutEntityServerResource<Thing> {

    private TapApplication app;
    private ThingRepo repository;

	protected void doInit() {
        super.doInit();
        app = (TapApplication) getApplication();
        repository = (ThingRepo) app.getRepository(Thing.class);
    }

    public Thing getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Thing> updateEntity(Thing entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ThingsResource.class);
    }
}
