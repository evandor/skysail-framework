package io.skysail.server.db.it.clip.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.*;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

public class PutClipResource extends PutEntityServerResource<Clip> {

    private ClipApplication app;

    protected void doInit() {
        super.doInit();
        app = (ClipApplication) getApplication();
    }

    public Clip getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

    public SkysailResponse<Clip> updateEntity(Clip entity) {
        Clip original = getEntity(null);
        original.setTitle(entity.getTitle());
        original.setModified(new Date());
        Object update = app.getRepository().update(getAttribute("id"), original);
        return new SkysailResponse<>();
    }

}
