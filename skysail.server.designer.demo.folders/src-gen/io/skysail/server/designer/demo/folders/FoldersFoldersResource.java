package io.skysail.server.designer.demo.folders;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class FoldersFoldersResource extends ListServerResource<io.skysail.server.designer.demo.folders.Folder> {

    private FoldersApplication app;
    private io.skysail.server.designer.demo.folders.FolderRepository oeRepo;

    public FoldersFoldersResource() {
        // super(Foldersio.skysail.server.designer.demo.folders.FolderResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list io.skysail.server.designer.demo.folders.Folders for Folder");
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        oeRepo = (io.skysail.server.designer.demo.folders.FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.Folder.class);
    }

    @Override
    public List<io.skysail.server.designer.demo.folders.Folder> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), oeRepo.count(filter));
        return oeRepo.find(filter, pagination);// .stream().filter(predicate);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FoldersFoldersResource.class, PostFoldersFolderRelationResource.class);
    }
}