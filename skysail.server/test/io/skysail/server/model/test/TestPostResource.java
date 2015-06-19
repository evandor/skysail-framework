package io.skysail.server.model.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class TestPostResource extends PutEntityServerResource<TestEntity>{

    @Override
    public SkysailResponse<?> updateEntity(TestEntity entity) {
        return null;
    }

    @Override
    public TestEntity getEntity() {
        return null;
    }

}
