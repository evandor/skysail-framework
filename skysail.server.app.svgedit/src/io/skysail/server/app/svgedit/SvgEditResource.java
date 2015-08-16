package io.skysail.server.app.svgedit;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SvgEditResource extends EntityServerResource<SvgDescriptor> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public SvgDescriptor getEntity() {
        return new SvgDescriptor();
    }

}
