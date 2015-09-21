package io.skysail.server.documentation.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class TestPutEntityResource extends PutEntityServerResource<Identifiable> {

    public TestPutEntityResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public SkysailResponse<Identifiable> updateEntity(Identifiable entity) {
        return new SkysailResponse<Identifiable>(null);
    }

    @Override
    public Identifiable getEntity() {
        return null;
    }

}
