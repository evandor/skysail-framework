package io.skysail.server.model.test;

import org.mockito.Mockito;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class TestPutResource extends PostEntityServerResource<TestEntity>{

    @Override
    public TestEntity createEntityTemplate() {
        return null;
    }

    @Override
    public void addEntity(TestEntity entity) {
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }



}
