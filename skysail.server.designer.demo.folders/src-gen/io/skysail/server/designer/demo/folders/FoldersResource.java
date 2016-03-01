package io.skysail.server.designer.demo.folders;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class FoldersResource extends ListServerResource<io.skysail.server.designer.demo.folders.Folder> {

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
        repository = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.Folder.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<io.skysail.server.designer.demo.folders.Folder> getEntity() {
        return (List<Folder>) repository.execute(Folder.class, "select * from " + DbClassName.of(Folder.class) + " where IN(Folders).size() = 0");
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostFolderResource.class);
    }
}