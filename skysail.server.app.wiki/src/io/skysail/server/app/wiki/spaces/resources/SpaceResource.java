package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class SpaceResource extends EntityServerResource<Space> {

    private String id;
    private WikiApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WikiApplication) getApplication();
    }
    
    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Space getEntity() {
         return app.getRepository().getById(Space.class, id);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(SpaceResource.class, PutSpaceResource.class);
    }

}
