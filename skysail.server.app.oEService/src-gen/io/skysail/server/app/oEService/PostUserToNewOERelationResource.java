package io.skysail.server.app.oEService;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.PostRelationResource2;

public class PostUserToNewOERelationResource extends PostRelationResource2<OE> {

    private OEServiceApplication app;
    private OERepository repo;
    private String parentId;

    public PostUserToNewOERelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        repo = (OERepository) app.getRepository(io.skysail.server.app.oEService.OE.class);
        parentId = getAttribute("id");
    }

    public OE createEntityTemplate() {
        return new OE();
    }

    @Override
    public void addEntity(OE entity) {
        OE parent = repo.findOne(parentId);
        parent.getOEs().add(entity);
        repo.save(parent, getApplication().getApplicationModel());
    }
}