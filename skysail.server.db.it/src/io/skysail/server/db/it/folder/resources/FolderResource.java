package io.skysail.server.db.it.folder.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.folder.*;
import io.skysail.server.restlet.resources.EntityServerResource;

public class FolderResource extends EntityServerResource<Folder> {

    private FolderApplication app;

    protected void doInit() {
        app = (FolderApplication) getApplication();
    }
    @Override
    public SkysailResponse<?> eraseEntity() {
        app.getRepository().delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public Folder getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

}
