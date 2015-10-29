package io.skysail.server.domain.core.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AThingResource extends EntityServerResource<AThing> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public AThing getEntity() {
        return null;
    }

}
