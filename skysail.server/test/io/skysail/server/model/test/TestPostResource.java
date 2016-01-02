package io.skysail.server.model.test;

import org.mockito.Mockito;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class TestPostResource extends PutEntityServerResource<TestEntity>{

    @Override
    public void updateEntity(TestEntity entity) {
    }

    @Override
    public TestEntity getEntity() {
        return null;
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

}
