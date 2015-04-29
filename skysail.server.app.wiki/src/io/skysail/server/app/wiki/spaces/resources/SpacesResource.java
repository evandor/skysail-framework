package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.resources.PostPageResource;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

public class SpacesResource extends ListServerResource<Map<String,Object>> {

    public SpacesResource() {
        super(SpaceResource.class, PostPageResource.class);
    }
    
    @Override
    public List<Map<String,Object>> getEntity() {
         return ((WikiApplication) getApplication()).getRepository().findAll(Space.class);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostSpaceResource.class);
    }
}
