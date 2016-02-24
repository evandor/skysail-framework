package io.skysail.server.designer.demo.folders;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
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

    @Override
    public List<io.skysail.server.designer.demo.folders.Folder> getEntity() {
        Filter filter = new Filter(getRequest());
        //filter.add("IN(folders).size()", "0");
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return (List<Folder>) repository.execute(Folder.class, "select * from " + DbClassName.of(Folder.class) + " where IN(folders).size() = 0");
        //return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostFolderResource.class,FoldersResource.class);
    }
}