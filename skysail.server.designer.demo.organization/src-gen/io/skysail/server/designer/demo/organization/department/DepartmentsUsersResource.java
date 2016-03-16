package io.skysail.server.designer.demo.organization.department;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.designer.demo.organization.*;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from relationResource.stg
 */
public class DepartmentsUsersResource extends ListServerResource<User> {

    private OrganizationApplicationGen app;
    private UserRepository oeRepo;

    public DepartmentsUsersResource() {
        super(DepartmentResourceGen.class);//, DepartmentsDepartmentResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (OrganizationApplication) getApplication();
        oeRepo = (UserRepository) app.getRepository(User.class);
    }

    @Override
    public List<User> getEntity() {
        return (List<User>) oeRepo.execute(User.class, "select * from " + DbClassName.of(User.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DepartmentsUsersResource.class, PostDepartmentsUserRelationResource.class);
    }
}