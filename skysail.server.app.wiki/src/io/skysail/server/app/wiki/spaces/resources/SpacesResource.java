package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.resources.PagesResource;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

public class SpacesResource extends ListServerResource<Space> {

    private WikiApplication app;

    public SpacesResource() {
        super(SpaceResource.class, PagesResource.class);
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<Space> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        return app.getRepository().findAll(Space.class, filter, "");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostSpaceResource.class);
    }
}
