package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

public class SpaceResource extends EntityServerResource<Map<String,Object>> {

    private String id;

    @Override
    protected void doInit() {
        id = getAttribute("id");
    }
    
    @Override
    public SkysailResponse eraseEntity() {
        return null;
    }

    @Override
    public Map<String,Object> getEntity() {
         return ((WikiApplication) getApplication()).getRepository().getById(Space.class, id);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(SpaceResource.class, PutSpaceResource.class);
    }

}
