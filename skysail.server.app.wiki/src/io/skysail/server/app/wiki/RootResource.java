package io.skysail.server.app.wiki;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.pages.resources.*;
import io.skysail.server.app.wiki.spaces.resources.*;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

public class RootResource extends ListServerResource<Identifiable> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostSpaceResource.class, SpacesResource.class, PostPageResource.class, PagesResource.class);
    }

    @Override
    public List<Identifiable> getEntity() {
        return Arrays.asList(new Identifiable() {

            @Override
            public String getId() {
                return null;
            }

            @Override
            public void setId(String id) {
            }});
    }
    


}