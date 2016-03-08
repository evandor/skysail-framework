package io.skysail.server.app.oEService.oe;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

/**
 * generated from targetRelationResource.stg
 */
public class OEsOEResource extends EntityServerResource<OE> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public OE getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(OEsOEsResource.class, PostOEsOERelationResource.class);
    }

}