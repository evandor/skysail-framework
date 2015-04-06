package io.skysail.server.documentation.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class TestPostEntityResource extends PostEntityServerResource<String> {

    public TestPostEntityResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public String createEntityTemplate() {
        return "template";
    }

    @Override
    public SkysailResponse<?> addEntity(String entity) {
        return new SkysailResponse<String>("added");
    }

}
