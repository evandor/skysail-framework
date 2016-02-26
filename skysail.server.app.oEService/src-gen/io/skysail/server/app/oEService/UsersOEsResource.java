package io.skysail.server.app.oEService;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class UsersOEsResource extends ListServerResource<io.skysail.server.app.oEService.OE> {

    private OEServiceApplication app;
    private io.skysail.server.app.oEService.OERepository oeRepo;

    public UsersOEsResource() {
        super(UserResource.class, UsersUserResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        oeRepo = (io.skysail.server.app.oEService.OERepository) app.getRepository(io.skysail.server.app.oEService.OE.class);
    }

    @Override
    public List<io.skysail.server.app.oEService.OE> getEntity() {
        return (List<OE>) oeRepo.execute(OE.class, "select * from " + DbClassName.of(io.skysail.server.app.oEService.OE.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(UsersOEsResource.class, PostUsersOERelationResource.class);
    }
}