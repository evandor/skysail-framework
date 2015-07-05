package io.skysail.server.model.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.mockito.Mockito;

public class TestPutResource extends PostEntityServerResource<TestEntity>{

    @Override
    public TestEntity createEntityTemplate() {
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(TestEntity entity) {
        return null;
    }
    
    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }



}
