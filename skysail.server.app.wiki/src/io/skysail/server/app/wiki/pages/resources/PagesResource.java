package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;
import java.util.Map;

public class PagesResource extends ListServerResource<Map<String,Object>> {

    public PagesResource() {
        super(PageResource.class);
    }
    
    @Override
    public List<Map<String,Object>> getEntity() {
         return ((WikiApplication) getApplication()).getRepository().findAll(Page.class);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPageResource.class);
    }
}
