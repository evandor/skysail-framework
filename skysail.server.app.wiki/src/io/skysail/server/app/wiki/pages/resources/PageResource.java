package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class PageResource extends EntityServerResource<Page> {

    private WikiApplication app;
    private String spaceId;
    private String pageId;

    protected void doInit() {
        spaceId = getAttribute("id");
        pageId = getAttribute("pageId");
        app = (WikiApplication) getApplication();
    }

    public Page getEntity() {
        return app.getRepository().getById(Page.class, pageId);
    }

    public List<Link> getLinks() {
        return super.getLinks(PutPageResource.class, PageResource.class, PostPageResource.class);
        //return super.getLinks(PostSubPageResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }


}
