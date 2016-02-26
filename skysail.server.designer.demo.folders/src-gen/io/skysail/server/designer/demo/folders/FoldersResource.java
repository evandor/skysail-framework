package io.skysail.server.designer.demo.folders;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class FoldersResource extends ListServerResource<io.skysail.server.designer.demo.folders.Folder> {

    private FoldersApplication app;
    private FolderRepository repository;

    public FoldersResource() {
        super(FolderResource.class);
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
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostFolderResource.class,FoldersResource.class);
    }
}