package io.skysail.server.model.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.mockito.Mockito;

public class TestEntityResource extends EntityServerResource<TestEntity>{

    @Override
    public TestEntity getEntity() {
        return new TestEntity();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

}
