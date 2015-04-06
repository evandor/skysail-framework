package io.skysail.server.documentation.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TestEntityResource extends EntityServerResource<String> {

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
    public String getEntity() {
        return null;
    }

}
