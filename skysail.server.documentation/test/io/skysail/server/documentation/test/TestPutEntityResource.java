package io.skysail.server.documentation.test;

import org.codehaus.jettison.json.JSONObject;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class TestPutEntityResource extends PutEntityServerResource<String> {

    public TestPutEntityResource() {
        setDescription("API description of class '" + this.getClass().getSimpleName() + "'");
    }

    // @Override
    // public JSONObject getEntity() {
    // return null;// "entity";
    // }

    @Override
    public SkysailResponse<?> updateEntity(String entity) {
        return new SkysailResponse<String>("resonse");
    }

    @Override
    public JSONObject getEntityAsJsonObject() {
        return null;
    }

    @Override
    public String getEntity() {
        return null;
    }

}
