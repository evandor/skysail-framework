package io.skysail.server.model.test;

import io.skysail.server.model.test.TestListOfEnumResource.MyEnum;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class TestListOfEnumResource extends ListServerResource<MyEnum>{

    public enum MyEnum {
        A,B,C;
    }

    @Override
    public List<MyEnum> getEntity() {
        return null;
    }

}
