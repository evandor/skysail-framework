package io.skysail.server.model.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class TestPutResource extends PostEntityServerResource<TestEntity>{

    @Override
    public TestEntity createEntityTemplate() {
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(TestEntity entity) {
        return null;
    }


}
