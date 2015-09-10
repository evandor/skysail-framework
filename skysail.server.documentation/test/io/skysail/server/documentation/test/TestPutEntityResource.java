package io.skysail.server.documentation.test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class TestPutEntityResource extends PutEntityServerResource<String> {

    public TestPutEntityResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    @Override
    public SkysailResponse<String> updateEntity(String entity) {
        return new SkysailResponse<String>("resonse");
    }

    @Override
    public String getEntity() {
        return null;
    }

}
