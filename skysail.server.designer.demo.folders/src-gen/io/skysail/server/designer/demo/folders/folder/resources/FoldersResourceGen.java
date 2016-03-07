package io.skysail.server.designer.demo.folders;

import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;

/**
 * generated from listResourceWithSelfReference.stg
 */
public class FoldersResource extends ListServerResource<io.skysail.server.designer.demo.folders.folder.Folder> {

    private FoldersApplication app;
    private FolderRepository repository;

    public FoldersResource() {
        super(FolderResource.class, FoldersFolderResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Folders");
    }

    public FoldersResource(Class<? extends FolderResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        repository = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class);
    }

    @Override
    public List<io.skysail.server.designer.demo.folders.folder.Folder> getEntity() {
        return (List<Folder>) repository.execute(Folder.class, "select * from " + DbClassName.of(Folder.class) + " where IN(Folders).size() = 0");
    }

    public List<Link> getLinks() {
              return super.getLinks(PostFolderResourceGen.class,FoldersResourceGen.class);
    }
}