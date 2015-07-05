package io.skysail.server.model.test;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.model.test.TestListOfEnumResource.MyEnum;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.mockito.Mockito;

public class TestListOfEnumResource extends ListServerResource<MyEnum>{

    public enum MyEnum {
        A,B,C;
    }

    @Override
    public List<MyEnum> getEntity() {
        return null;
    }
    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

}
