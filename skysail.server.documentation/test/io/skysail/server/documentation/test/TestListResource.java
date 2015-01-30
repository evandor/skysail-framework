package io.skysail.server.documentation.test;

import io.skysail.api.documentation.API;

import java.util.Arrays;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class TestListResource extends ListServerResource<String> {

    public TestListResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    @API(desc = "description of getData method")
    public List<String> getData() {
        return Arrays.asList("hi");
    }

}