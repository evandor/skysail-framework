package io.skysail.server.documentation.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class TestListResource extends ListServerResource<Identifiable> {

    public TestListResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public List<Identifiable> getEntity() {
        return null;
    }


}