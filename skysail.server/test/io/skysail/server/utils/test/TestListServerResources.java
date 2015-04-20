package io.skysail.server.utils.test;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.Arrays;
import java.util.List;

public class TestListServerResources extends ListServerResource<String> {

    @Override
    public List<String> getEntity() {
        return Arrays.asList("str");
    }

}