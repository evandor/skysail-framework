package io.skysail.server.designer.demo.folders.folder.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.designer.demo.folders.*;

/**
 * generated from entityResource.stg
 */
public class FolderResourceGen extends EntityServerResource<io.skysail.server.designer.demo.folders.folder.Folder> {

    private String id;
    private FoldersApplication app;
    private FolderRepository repository;

    public FolderResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (FoldersApplication) getApplication();
        repository = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.demo.folders.folder.Folder getEntity() {
        return (io.skysail.server.designer.demo.folders.folder.Folder)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutFolderResourceGen.class,PostFolderResourceGen.class,FoldersResourceGen.class);
    }

}