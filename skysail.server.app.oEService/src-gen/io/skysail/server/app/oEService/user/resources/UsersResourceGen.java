package io.skysail.server.app.oEService.user.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.oEService.*;
import io.skysail.server.app.oEService.oe.resources.OEsResourceGen;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

/**
 * generated from listResource.stg
 */
public class UsersResourceGen extends ListServerResource<io.skysail.server.app.oEService.user.User> {

    private OEServiceApplication app;
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
        app = (OEServiceApplication) getApplication();
        repository = (UserRepository) app.getRepository(io.skysail.server.app.oEService.user.User.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.oEService.user.User> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostUserResourceGen.class,OEsResourceGen.class,UsersResourceGen.class);
    }
}