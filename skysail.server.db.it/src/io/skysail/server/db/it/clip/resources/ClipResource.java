package io.skysail.server.db.it.clip.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ClipResource extends EntityServerResource<Clip> {

    private ClipApplication app;
    private ClipRepository repository;

    protected void doInit() {
        app = (ClipApplication) getApplication();
        repository = (ClipRepository) app.getRepository(Clip.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public Clip getEntity() {
        return repository.findOne(getAttribute("id"));
    }

}
