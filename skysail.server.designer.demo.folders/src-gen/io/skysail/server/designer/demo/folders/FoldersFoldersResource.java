package io.skysail.server.designer.demo.folders;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class FoldersFoldersResource extends ListServerResource<io.skysail.server.designer.demo.folders.Folder> {

    private FoldersApplication app;
    private io.skysail.server.designer.demo.folders.FolderRepository oeRepo;

    public FoldersFoldersResource() {
        super(FolderResource.class, FoldersFolderResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        oeRepo = (io.skysail.server.designer.demo.folders.FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.Folder.class);
    }

    @Override
    public List<io.skysail.server.designer.demo.folders.Folder> getEntity() {
        return (List<Folder>) oeRepo.execute(Folder.class, "select * from " + DbClassName.of(io.skysail.server.designer.demo.folders.Folder.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FoldersFoldersResource.class);
    }
}