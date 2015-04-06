package io.skysail.server.documentation.test;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class TestListResource extends ListServerResource<String> {

    public TestListResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public List<String> getEntity() {
        return null;
    }


}