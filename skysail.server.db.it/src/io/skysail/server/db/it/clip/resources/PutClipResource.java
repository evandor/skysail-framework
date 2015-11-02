package io.skysail.server.db.it.clip.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

public class PutClipResource extends PutEntityServerResource<Clip> {

    private ClipApplication app;
    private ClipRepository repository;

    protected void doInit() {
        super.doInit();
        app = (ClipApplication) getApplication();
        repository = (ClipRepository) app.getRepository(Clip.class);
    }

    public Clip getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Clip> updateEntity(Clip entity) {
        Clip original = getEntity(null);
        original.setTitle(entity.getTitle());
        original.setModified(new Date());
        Object update = repository.update(getAttribute("id"), original);
        return new SkysailResponse<>();
    }

}
