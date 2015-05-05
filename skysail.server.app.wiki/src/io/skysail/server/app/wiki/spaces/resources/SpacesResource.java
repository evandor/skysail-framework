package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.resources.PagesResource;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;
import java.util.Map;

import org.restlet.resource.ResourceException;

public class SpacesResource extends ListServerResource<Map<String,Object>> {

    private WikiApplication app;

    public SpacesResource() {
        super(SpaceResource.class,PagesResource.class);
    }
    
    @Override
    protected void doInit() throws ResourceException {
        app = (WikiApplication) getApplication();
    }
    
    @Override
    public List<Map<String,Object>> getEntity() {
         return app.getRepository().findAll(Space.class);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostSpaceResource.class);
    }
}
