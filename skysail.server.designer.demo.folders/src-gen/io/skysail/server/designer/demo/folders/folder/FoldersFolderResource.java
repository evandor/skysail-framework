package io.skysail.server.designer.demo.folders.folder;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from targetRelationResource.stg
 */
public class FoldersFolderResource extends EntityServerResource<Folder> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Folder getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FoldersFoldersResource.class, PostFoldersFolderRelationResource.class);
    }

}