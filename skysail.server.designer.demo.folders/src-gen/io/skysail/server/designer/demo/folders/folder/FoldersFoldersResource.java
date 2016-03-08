package io.skysail.server.designer.demo.folders.folder;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.designer.demo.folders.*;

import io.skysail.server.designer.demo.folders.folder.*;
import io.skysail.server.designer.demo.folders.folder.resources.*;


/**
 * generated from relationResource.stg
 */
public class FoldersFoldersResource extends ListServerResource<Folder> {

    private FoldersApplicationGen app;
    private FolderRepository oeRepo;

    public FoldersFoldersResource() {
        super(FolderResourceGen.class);//, FoldersFolderResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        oeRepo = (FolderRepository) app.getRepository(Folder.class);
    }

    @Override
    public List<Folder> getEntity() {
        return (List<Folder>) oeRepo.execute(Folder.class, "select * from " + DbClassName.of(Folder.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FoldersFoldersResource.class, PostFoldersFolderRelationResource.class);
    }
}