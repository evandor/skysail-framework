package de.twenty11.skysail.products.website;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class WebsiteRootResource extends EntityServerResource<Dummy> {

    @Override
    public Dummy getEntity() {
        return new Dummy();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
