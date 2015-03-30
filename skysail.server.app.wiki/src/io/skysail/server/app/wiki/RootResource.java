package io.skysail.server.app.wiki;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.spaces.PostSpaceResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

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