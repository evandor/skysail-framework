package io.skysail.server.designer.demo.folders;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from entityResource.stg
 */
public class FolderResource extends EntityServerResource<io.skysail.server.designer.demo.folders.Folder> {

    private String id;
    private FoldersApplication app;
    private FolderRepository repository;

    public FolderResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (FoldersApplication) getApplication();
        repository = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.Folder.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.demo.folders.Folder getEntity() {
        return (io.skysail.server.designer.demo.folders.Folder)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutFolderResource.class,PostFolderResource.class,FoldersResource.class);
    }

}