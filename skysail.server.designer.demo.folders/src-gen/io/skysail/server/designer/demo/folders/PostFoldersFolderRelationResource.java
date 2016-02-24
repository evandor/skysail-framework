package io.skysail.server.designer.demo.folders;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

public class PostFoldersFolderRelationResource extends PostRelationResource<io.skysail.server.designer.demo.folders.Folder, io.skysail.server.designer.demo.folders.Folder> {

    private FoldersApplication app;
    private FolderRepository repo;
    //private UserRepository userRepo;

    public PostFoldersFolderRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (FoldersApplication) getApplication();
        repo = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.Folder.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<Folder> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repo.count(filter));
        return repo.find(filter, pagination);
    }

    @Override
    protected List<Folder> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repo.count(filter));
        return repo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<Folder> entities) {
        String id = getAttribute("id");
        io.skysail.server.designer.demo.folders.Folder theUser = repo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        repo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.designer.demo.folders.Folder theUser, Folder e) {
        if (!theUser.getFolders().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getFolders().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}