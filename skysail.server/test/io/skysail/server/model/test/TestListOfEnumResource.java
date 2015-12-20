package io.skysail.server.model.test;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.model.test.TestListOfEnumResource.MyEnum;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.mockito.Mockito;

public class TestListOfEnumResource extends ListServerResource<MyEnum>{

    public enum MyEnum implements Identifiable {
        A,B,C;

        @Override
        public String getId() {
            return null;
        }

        @Override
        public void setId(String id) {
        }
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
