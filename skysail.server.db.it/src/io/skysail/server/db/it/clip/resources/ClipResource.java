package io.skysail.server.db.it.clip.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.*;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ClipResource extends EntityServerResource<Clip> {

    private ClipApplication app;

    protected void doInit() {
        app = (ClipApplication) getApplication();
    }
    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Clip getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

}
