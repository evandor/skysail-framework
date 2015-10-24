package io.skysail.server.app.tap;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutThingResource extends PutEntityServerResource<Thing> {

    private io.skysail.server.app.tap.TabApplication app;

	protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.tap.TabApplication) getApplication();
    }

    public Thing getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

    public SkysailResponse<Thing> updateEntity(Thing entity) {
        app.getRepository().update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }
}
