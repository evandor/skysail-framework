package io.skysail.server.designer.demo.organization.user.resources;

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
public class UsersResourceGen extends ListServerResource<io.skysail.server.designer.demo.organization.user.User> {

    private OrganizationApplication app;
    private UserRepository repository;

    public UsersResourceGen() {
        super(UserResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Users");
    }

    public UsersResourceGen(Class<? extends UserResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (OrganizationApplication) getApplication();
        repository = (UserRepository) app.getRepository(io.skysail.server.designer.demo.organization.user.User.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.designer.demo.organization.user.User> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostUserResourceGen.class,DepartmentsResourceGen.class,UsersResourceGen.class);
    }
}