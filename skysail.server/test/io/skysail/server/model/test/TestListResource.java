package io.skysail.server.model.test;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import lombok.NoArgsConstructor;

import org.mockito.Mockito;

@NoArgsConstructor
public class TestListResource extends ListServerResource<TestEntity>{
    
    @Override
    public List<TestEntity> getEntity() {
        return null;
    }
    
    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

}
