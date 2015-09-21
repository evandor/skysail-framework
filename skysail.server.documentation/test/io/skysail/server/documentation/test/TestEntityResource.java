package io.skysail.server.documentation.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TestEntityResource extends EntityServerResource<Identifiable> {

    public TestEntityResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    // @Override
    // public String getData() {
    // return "data";
    // }

    @Override
    public String getId() {
        return "id";
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Identifiable getEntity() {
        return null;
    }

}
