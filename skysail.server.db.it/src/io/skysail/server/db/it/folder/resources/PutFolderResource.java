package io.skysail.server.db.it.folder.resources;

import java.util.Date;

import io.skysail.server.db.it.folder.*;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutFolderResource extends PutEntityServerResource<Folder> {

    private FolderApplication app;

    protected void doInit() {
        super.doInit();
        app = (FolderApplication) getApplication();
    }

    public Folder getEntity() {
        return app.getRepository().findOne(getAttribute("id"));
    }

    public void updateEntity(Folder entity) {
        Folder original = getEntity(null);
        original.setName(entity.getName());
        original.setModified(new Date());
        app.getRepository().update(getAttribute("id"), original);
    }

}
