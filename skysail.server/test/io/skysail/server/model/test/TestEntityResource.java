package io.skysail.server.model.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TestEntityResource extends EntityServerResource<TestEntity>{

    @Override
    public TestEntity getEntity() {
        return new TestEntity();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
