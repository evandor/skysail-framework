package io.skysail.server.app.wiki;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SpacesResource extends ListServerResource<io.skysail.server.app.wiki.Space> {

    private WikiApplication app;
    private SpaceRepository repository;

    public SpacesResource() {
        super(SpaceResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Spaces");
    }

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
        repository = (SpaceRepository) app.getRepository(io.skysail.server.app.wiki.Space.class);
    }

    @Override
    public Set<String> getRestrictedToMediaTypes() {
        return super.getRestrictedToMediaTypes("text/prs.skysail-uikit");
    }

    @Override
    public List<io.skysail.server.app.wiki.Space> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
              return super.getLinks(PostSpaceResource.class,SpacesResource.class);
    }
}