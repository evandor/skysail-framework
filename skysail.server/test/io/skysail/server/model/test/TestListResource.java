package io.skysail.server.model.test;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class TestListResource extends ListServerResource<TestEntity>{

    @Override
    public List<TestEntity> getEntity() {
        return null;
    }

}
