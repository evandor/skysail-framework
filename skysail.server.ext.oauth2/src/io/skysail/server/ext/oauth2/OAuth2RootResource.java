package io.skysail.server.ext.oauth2;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class OAuth2RootResource extends EntityServerResource<Empty> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Empty getEntity() {
        return new Empty();
    }

}
