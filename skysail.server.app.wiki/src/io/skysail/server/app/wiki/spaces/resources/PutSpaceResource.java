package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Map;

public class PutSpaceResource extends PutEntityServerResource<Map<String,Object>> {

    private String id;

    @Override
    protected void doInit() {
        id = getAttribute("id");
    }
    
    @Override
    public Map<String,Object> getEntity() {
        return ((WikiApplication) getApplication()).getRepository().getById(Space.class, id);
    }

    @Override
    public SkysailResponse updateEntity(Map<String,Object> entity) {
        return new SkysailResponse<>();
    }


}
