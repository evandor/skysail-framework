package io.skysail.server.app.wiki;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.pages.resources.PagesResource;
import io.skysail.server.app.wiki.pages.resources.PostPageResource;
import io.skysail.server.app.wiki.spaces.resources.PostSpaceResource;
import io.skysail.server.app.wiki.spaces.resources.SpacesResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostSpaceResource.class, SpacesResource.class, PostPageResource.class, PagesResource.class);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }
    


}