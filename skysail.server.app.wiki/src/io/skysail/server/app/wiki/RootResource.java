package io.skysail.server.app.wiki;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class RootResource extends ListServerResource<String> {

//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostSpaceResource.class, SpacesResource.class, PostPageResource.class, PagesResource.class);
//    }

    @Override
    public List<String> getEntity() {
        return null;
    }
    


}