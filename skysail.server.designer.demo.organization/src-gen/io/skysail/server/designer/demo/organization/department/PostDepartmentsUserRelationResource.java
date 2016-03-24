package io.skysail.server.designer.demo.organization.department;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource;

import io.skysail.server.designer.demo.organization.*;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from postRelationResource.stg
 */
public class PostDepartmentsUserRelationResource extends PostRelationResource<io.skysail.server.designer.demo.organization.department.Department, io.skysail.server.designer.demo.organization.user.User> {

    private OrganizationApplicationGen app;
    private UserRepository UserRepo;
    private DepartmentRepository DepartmentRepo;

    public PostDepartmentsUserRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OrganizationApplicationGen) getApplication();
        UserRepo = (UserRepository) app.getRepository(io.skysail.server.designer.demo.organization.user.User.class);
        //userRepo = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    public List<User> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), UserRepo.count(filter));
        return UserRepo.find(filter, pagination);
    }

    @Override
    protected List<User> getRelationTargets(String selectedValues) {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), UserRepo.count(filter));
        return UserRepo.find(filter, pagination);//.stream().filter(predicate);
    }

    @Override
    public void addRelations(List<User> entities) {
        String id = getAttribute("id");
        io.skysail.server.designer.demo.organization.department.Department theUser = DepartmentRepo.findOne(id);
        entities.stream().forEach(e -> addIfNotPresentYet(theUser, e));
        UserRepo.save(theUser, getApplication().getApplicationModel());
    }

    private void addIfNotPresentYet(io.skysail.server.designer.demo.organization.department.Department theUser, User e) {
        if (!theUser.getUsers().stream().filter(oe -> oe.getId().equals(oe.getId())).findFirst().isPresent()) {
            theUser.getUsers().add(e);
        }
    }



//    @Override
//    public String redirectTo() {
//        return super.redirectTo(UsersResource.class);
//    }


}