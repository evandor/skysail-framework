package io.skysail.server.app.wiki.spaces;

import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;
import java.util.Map;

public class SpacesResource extends ListServerResource<Map<String,Object>> {

    @Override
    public List<Map<String,Object>> getEntity() {
         return ((WikiApplication) getApplication()).getRepository().findAll(Space.class);
    }
}
