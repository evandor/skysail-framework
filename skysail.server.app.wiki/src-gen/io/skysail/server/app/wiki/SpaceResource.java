package io.skysail.server.app.wiki;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SpaceResource extends EntityServerResource<io.skysail.server.app.wiki.Space> {

    private String id;
    private WikiApplication app;
    private SpaceRepository repository;

    public SpaceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WikiApplication) getApplication();
        repository = (SpaceRepository) app.getRepository(io.skysail.server.app.wiki.Space.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.wiki.Space getEntity() {
        return (io.skysail.server.app.wiki.Space)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutSpaceResource.class,PostPageResource.class,PagesResource.class);
    }

}