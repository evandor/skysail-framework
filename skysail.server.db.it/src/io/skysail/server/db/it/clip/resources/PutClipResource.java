package io.skysail.server.db.it.clip.resources;

import java.util.Date;

import io.skysail.server.db.it.clip.Clip;
import io.skysail.server.db.it.clip.ClipApplication;
import io.skysail.server.db.it.clip.ClipRepository;
import io.skysail.server.restlet.resources.PutEntityServerResource;

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

    public void updateEntity(Clip entity) {
        Clip original = getEntity(null);
        original.setTitle(entity.getTitle());
        original.setModified(new Date());
        Object update = repository.update(original, app.getApplicationModel());
    }

}
