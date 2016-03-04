package io.skysail.server.designer.demo.folders;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource2;

/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostFolderToNewFolderRelationResource extends PostRelationResource2<Folder> {

    private FoldersApplication app;
    private FolderRepository repo;
    private String parentId;

    public PostFolderToNewFolderRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        repo = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.Folder.class);
        parentId = getAttribute("id");
    }

    public Folder createEntityTemplate() {
        return new Folder();
    }

    @Override
    public void addEntity(Folder entity) {
        Folder parent = repo.findOne(parentId);
        parent.getFolders().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }
}