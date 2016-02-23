package io.skysail.server.app.oEService;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class UsersOEsResource extends ListServerResource<io.skysail.server.app.oEService.OE> {

    private io.skysail.server.app.oEService.OEServiceApplication app;
    private io.skysail.server.app.oEService.OERepository oeRepo;

    public UsersOEsResource() {
        // super(Usersio.skysail.server.app.oEService.OEResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list io.skysail.server.app.oEService.OEs for User");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        oeRepo = (io.skysail.server.app.oEService.OERepository) app.getRepository(io.skysail.server.app.oEService.OE.class);
    }

    @Override
    public List<io.skysail.server.app.oEService.OE> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), oeRepo.count(filter));
        return oeRepo.find(filter, pagination);// .stream().filter(predicate);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(UsersOEsResource.class, PostUsersOERelationResource.class);
    }
}