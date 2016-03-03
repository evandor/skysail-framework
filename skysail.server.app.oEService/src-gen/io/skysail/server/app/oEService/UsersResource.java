package io.skysail.server.app.oEService;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;

/**
 * generated from listResource.stg
 */
public class UsersResource extends ListServerResource<io.skysail.server.app.oEService.User> {

    private OEServiceApplication app;
    private UserRepository repository;

    public UsersResource() {
        super(UserResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Users");
    }

    public UsersResource(Class<? extends UserResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        repository = (UserRepository) app.getRepository(io.skysail.server.app.oEService.User.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.oEService.User> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostUserResource.class,OEsResource.class,UsersResource.class);
    }
}