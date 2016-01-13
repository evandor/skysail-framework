package io.skysail.server.app.wiki;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class PageResource extends EntityServerResource<io.skysail.server.app.wiki.Page> {

    private String id;
    private WikiApplication app;
    //private PageRepository repository;

    public PageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WikiApplication) getApplication();
      //  repository = (PageRepository) app.getRepository(io.skysail.server.app.wiki.Page.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        //repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.wiki.Page getEntity() {
        return (io.skysail.server.app.wiki.Page)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutPageResource.class);
    }

}