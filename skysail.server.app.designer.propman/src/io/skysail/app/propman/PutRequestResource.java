package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;

public class PutRequestResource extends PutEntityServerResource<Request> {

    @Override
    public SkysailResponse<?> updateEntity(Request entity) {
        return null;
    }

    @Override
    public Request getEntity() {
        return null;
    }
}