package io.skysail.server.domain.core.test;

import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostAThingResource extends PostEntityServerResource<AThing> {

    @Override
    public AThing createEntityTemplate() {
        return null;
    }

}
