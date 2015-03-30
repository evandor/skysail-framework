package io.skysail.server.documentation.test;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class TestListResource extends ListServerResource<String> {

    public TestListResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public List<String> getEntity() {
        return null;
    }


}