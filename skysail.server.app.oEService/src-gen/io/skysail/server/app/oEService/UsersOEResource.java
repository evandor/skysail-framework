package io.skysail.server.app.oEService;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class UsersOEResource extends EntityServerResource<OE> {

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
        return super.getLinks(UsersOEsResource.class, PostUsersOERelationResource.class);
    }

}
