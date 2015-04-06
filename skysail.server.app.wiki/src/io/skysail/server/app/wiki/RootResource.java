package io.skysail.server.app.wiki;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.spaces.PostSpaceResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PostSpaceResource.class);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

}