package io.skysail.server.app.oEService;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

/**
 * generated from listResource.stg
 */
public class UsersOEsResource extends ListServerResource<OE> {

    private OEServiceApplication app;
    private OERepository oeRepo;

    public UsersOEsResource() {
        super(UserResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "["+this.getClass().getSimpleName()+"]");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        oeRepo = (io.skysail.server.app.oEService.OERepository) app.getRepository(OE.class);
    }

    @Override
    public List<OE> getEntity() {
        return (List<OE>) oeRepo.execute(OE.class, "select * from " + DbClassName.of(OE.class) + " where #"+getAttribute("id")+" in IN(folders)");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(UsersOEsResource.class, PostUsersOERelationResource.class);
    }
}