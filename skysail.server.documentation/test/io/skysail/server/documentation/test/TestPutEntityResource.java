package io.skysail.server.documentation.test;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class TestPutEntityResource extends PutEntityServerResource<String> {

    public TestPutEntityResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public SkysailResponse<?> updateEntity(String entity) {
        return new SkysailResponse<String>("resonse");
    }

    @Override
    public String getEntity() {
        return null;
    }

}
