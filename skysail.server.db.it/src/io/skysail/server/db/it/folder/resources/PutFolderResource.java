package io.skysail.server.db.it.folder.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.folder.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

public class PutFolderResource extends PutEntityServerResource<Folder> {

    private FolderApplication app;

    protected void doInit() {
        super.doInit();
        app = (FolderApplication) getApplication();
    }

    public Folder getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

    public SkysailResponse<Folder> updateEntity(Folder entity) {
        Folder original = getEntity(null);
        original.setName(entity.getName());
        original.setModified(new Date());
        Object update = app.getRepository().update(getAttribute("id"), original);
        return new SkysailResponse<>();
    }

}
