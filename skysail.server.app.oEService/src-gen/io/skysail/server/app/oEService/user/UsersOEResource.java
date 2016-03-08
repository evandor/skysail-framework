package io.skysail.server.app.oEService.user;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import io.skysail.server.app.oEService.oe.*;
import io.skysail.server.app.oEService.oe.resources.*;
import io.skysail.server.app.oEService.user.*;
import io.skysail.server.app.oEService.user.resources.*;


/**
 * generated from targetRelationResource.stg
 */
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