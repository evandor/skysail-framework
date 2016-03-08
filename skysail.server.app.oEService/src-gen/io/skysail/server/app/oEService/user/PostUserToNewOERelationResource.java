package io.skysail.server.app.oEService.user;

import io.skysail.server.app.oEService.*;
import io.skysail.server.app.oEService.oe.OE;
import io.skysail.server.restlet.resources.PostRelationResource2;

/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostUserToNewOERelationResource extends PostRelationResource2<OE> {

    private OEServiceApplicationGen app;
    private OERepository repo;
    private String parentId;

    public PostUserToNewOERelationResource() {
        // addToContext(ResourceContextId.LINK_TITLE, "add");
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        repo = (OERepository) app.getRepository(io.skysail.server.app.oEService.oe.OE.class);
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