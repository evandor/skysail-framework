package io.skysail.server.db.it.clip.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.clip.Clip;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.Date;

public class PostClipResource extends PostEntityServerResource<Clip> {

    @Override
    public Clip createEntityTemplate() {
        return new Clip();
    }

    @Override
    public SkysailResponse<Clip> addEntity(Clip entity) {
        entity.setCreated(new Date());
        return super.addEntity(entity);
    }
}
