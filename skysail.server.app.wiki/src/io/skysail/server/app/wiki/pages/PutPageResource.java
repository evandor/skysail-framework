package io.skysail.server.app.wiki.pages;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Map;

public class PutPageResource extends PutEntityServerResource<Map<String,Object>> {

    private String id;

    @Override
    protected void doInit() {
        id = getAttribute("id");
    }
    
    @Override
    public SkysailResponse<?> updateEntity(Map<String,Object> entity) {
        return null;
    }

    @Override
    public Map<String,Object> getEntity() {
        return ((WikiApplication)getApplication()).getRepository().getById(Page.class, id);
    }

}
