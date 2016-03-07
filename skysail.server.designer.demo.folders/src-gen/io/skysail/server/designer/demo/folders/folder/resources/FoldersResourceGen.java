package io.skysail.server.designer.demo.folders.folder.resources;

import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.folders.*;

/**
 * generated from listResourceWithSelfReference.stg
 */
public class FoldersResourceGen extends ListServerResource<io.skysail.server.designer.demo.folders.folder.Folder> {

    private FoldersApplication app;
    private FolderRepository repository;

    public FoldersResourceGen() {
        super(FolderResourceGen.class);//, FoldersFolderResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Folders");
    }

    public FoldersResourceGen(Class<? extends FolderResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        repository = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class);
    }

    @Override
    public List<io.skysail.server.designer.demo.folders.folder.Folder> getEntity() {
        return (List<io.skysail.server.designer.demo.folders.folder.Folder>) repository.execute(io.skysail.server.designer.demo.folders.folder.Folder.class, "select * from " + DbClassName.of(io.skysail.server.designer.demo.folders.folder.Folder.class) + " where IN(Folders).size() = 0");
    }

    public List<Link> getLinks() {
              return super.getLinks(PostFolderResourceGen.class,FoldersResourceGen.class);
    }
}