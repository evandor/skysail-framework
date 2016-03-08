package io.skysail.server.app.oEService.oe.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.app.oEService.*;

/**
 * generated from entityResource.stg
 */
public class OEResourceGen extends EntityServerResource<io.skysail.server.app.oEService.oe.OE> {

    private String id;
    private OEServiceApplication app;
    private OERepository repository;

    public OEResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (OEServiceApplication) getApplication();
        repository = (OERepository) app.getRepository(io.skysail.server.app.oEService.oe.OE.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.oEService.oe.OE getEntity() {
        return (io.skysail.server.app.oEService.oe.OE)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutOEResourceGen.class,PostOEResourceGen.class,OEsResourceGen.class);
    }

}