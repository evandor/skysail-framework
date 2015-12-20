package io.skysail.server.utils.test;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.mockito.Mockito;

public class TestListServerResource extends ListServerResource<Identifiable> {

    @Override
    public List<Identifiable> getEntity() {
        return Arrays.asList(new Identifiable() {

            @Override
            public String getId() {
                return null;
            }

            @Override
            public void setId(String id) {
            }

        });
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }


}