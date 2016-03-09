package io.skysail.server.app.oEService.oe;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.PostRelationResource2;
import io.skysail.server.app.oEService.*;

import io.skysail.server.app.oEService.oe.*;
import io.skysail.server.app.oEService.oe.resources.*;
import io.skysail.server.app.oEService.user.*;
import io.skysail.server.app.oEService.user.resources.*;


/**
 * generated from postRelationToNewEntityResource.stg
 */
public class PostOEToNewOERelationResource extends PostRelationResource2<OE> {

    private OEServiceApplicationGen app;
    private OERepository repo;
    private String parentId;

    public PostOEToNewOERelationResource() {
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

    @Override
    public List<Link> getLinks() {
        return super.getLinks(OEsOEsResource.class, PostOEToNewOERelationResource.class);
    }
}