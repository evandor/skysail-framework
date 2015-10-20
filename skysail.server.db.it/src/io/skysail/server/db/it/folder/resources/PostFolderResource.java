package io.skysail.server.db.it.folder.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.folder.Folder;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.Date;

public class PostFolderResource extends PostEntityServerResource<Folder> {

    @Override
    public Folder createEntityTemplate() {
        return new Folder();
    }

    @Override
    public SkysailResponse<Folder> addEntity(Folder entity) {
        entity.setCreated(new Date());
        return super.addEntity(entity);
    }
}
