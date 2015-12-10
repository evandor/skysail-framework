package io.skysail.server.db.it.clip.resources;

import java.util.Date;

import io.skysail.server.db.it.clip.Clip;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostClipResource extends PostEntityServerResource<Clip> {

    @Override
    public Clip createEntityTemplate() {
        return new Clip();
    }

    @Override
    public void addEntity(Clip entity) {
        entity.setCreated(new Date());
    }
}
