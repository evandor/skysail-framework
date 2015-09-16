package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class SpacesResource extends ListServerResource<Space> {

    private WikiApplication app;

    public SpacesResource() {
       super(SpaceResource.class);
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<Space> getEntity() {
        Filter filter = new Filter(getRequest());
        return app.getSpacesRepo().find(filter);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostSpaceResource.class);
    }
}
