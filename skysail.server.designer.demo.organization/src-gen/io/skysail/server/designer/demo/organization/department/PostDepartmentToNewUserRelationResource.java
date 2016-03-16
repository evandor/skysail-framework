package io.skysail.server.designer.demo.organization.department;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.PostRelationResource2;
import io.skysail.server.designer.demo.organization.*;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostDepartmentToNewUserRelationResource extends PostRelationResource2<User> {

    private OrganizationApplicationGen app;
    private DepartmentRepository repo;
    private String parentId;

    public PostDepartmentToNewUserRelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OrganizationApplication) getApplication();
        repo = (DepartmentRepository) app.getRepository(io.skysail.server.designer.demo.organization.department.Department.class);
        parentId = getAttribute("id");
    }

    public User createEntityTemplate() {
        return new User();
    }

    @Override
    public void addEntity(User entity) {
        Department parent = repo.findOne(parentId);
        parent.getUsers().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DepartmentsUsersResource.class, PostDepartmentToNewUserRelationResource.class);
    }
}