package io.skysail.server.db.it.folder.resources;

import java.util.Date;

import io.skysail.server.db.it.folder.*;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostFolderResource extends PostEntityServerResource<Folder> {

    @Override
    public Folder createEntityTemplate() {
        return new Folder();
    }

    @Override
    public void addEntity(Folder entity) {
        entity.setCreated(new Date());
        String id = ((FolderApplication)getApplication()).getRepository().save(entity, "subfolders").getId().toString();
        entity.setId(id);
    }
}
