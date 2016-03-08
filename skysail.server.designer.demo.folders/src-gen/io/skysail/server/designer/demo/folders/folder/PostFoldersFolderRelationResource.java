package io.skysail.server.designer.demo.folders.folder;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

import io.skysail.server.designer.demo.folders.*;

import io.skysail.server.designer.demo.folders.folder.*;
import io.skysail.server.designer.demo.folders.folder.resources.*;


/**
 * generated from postSelfRelationResource.stg
 */
public class PostFoldersFolderRelationResource extends PostRelationResource<io.skysail.server.designer.demo.folders.folder.Folder, io.skysail.server.designer.demo.folders.folder.Folder> {

    private FoldersApplicationGen app;
    private FolderRepository FolderRepo;

    public PostFoldersFolderRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (FoldersApplicationGen) getApplication();
        FolderRepo = (FolderRepository) app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<Folder> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), FolderRepo.count(filter));
        return FolderRepo.find(filter, pagination);
    }

    @Override
    protected List<Folder> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), FolderRepo.count(filter));
        return FolderRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<Folder> entities) {
        String id = getAttribute("id");
        io.skysail.server.designer.demo.folders.folder.Folder theUser = FolderRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        FolderRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.designer.demo.folders.folder.Folder theUser, Folder e) {
        if (!theUser.getFolders().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getFolders().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}