package io.skysail.server.documentation.test;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class TestListResource extends ListServerResource<String> {

    public TestListResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    // @Override
    // @API(desc = "description of getData method")
    // public List<String> getData() {
    // return Arrays.asList("hi");
    // }

}