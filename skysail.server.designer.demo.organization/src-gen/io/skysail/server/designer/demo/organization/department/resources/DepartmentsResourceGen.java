package io.skysail.server.designer.demo.organization.department.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.designer.demo.organization.*;

import io.skysail.server.designer.demo.organization.department.*;
import io.skysail.server.designer.demo.organization.department.resources.*;
import io.skysail.server.designer.demo.organization.user.*;
import io.skysail.server.designer.demo.organization.user.resources.*;


/**
 * generated from listResource.stg
 */
public class DepartmentsResourceGen extends ListServerResource<io.skysail.server.designer.demo.organization.department.Department> {

    private OrganizationApplication app;
    private DepartmentRepository repository;

    public DepartmentsResourceGen() {
        super(DepartmentResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Departments");
    }

    public DepartmentsResourceGen(Class<? extends DepartmentResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (OrganizationApplication) getApplication();
        repository = (DepartmentRepository) app.getRepository(io.skysail.server.designer.demo.organization.department.Department.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.designer.demo.organization.department.Department> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostDepartmentResourceGen.class,DepartmentsResourceGen.class,UsersResourceGen.class);
    }
}