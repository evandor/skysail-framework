package io.skysail.server.utils.test;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.mockito.Mockito;

public class TestListServerResources extends ListServerResource<String> {

    @Override
    public List<String> getEntity() {
        return Arrays.asList("str");
    }
    
    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }


}