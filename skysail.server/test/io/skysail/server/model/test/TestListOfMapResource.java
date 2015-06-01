package io.skysail.server.model.test;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

public class TestListOfMapResource extends ListServerResource<Map<String,Object>>{

    @Override
    public List<Map<String,Object>> getEntity() {
        return null;
    }

}
