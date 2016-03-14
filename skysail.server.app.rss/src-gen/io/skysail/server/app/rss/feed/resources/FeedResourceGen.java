package io.skysail.server.app.rss.feed.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.app.rss.*;

import io.skysail.server.app.rss.feed.*;
import io.skysail.server.app.rss.feed.resources.*;


/**
 * generated from entityResource.stg
 */
public class FeedResourceGen extends EntityServerResource<io.skysail.server.app.rss.feed.Feed> {

    private String id;
    private RSSApplication app;
    private FeedRepository repository;

    public FeedResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (RSSApplication) getApplication();
        repository = (FeedRepository) app.getRepository(io.skysail.server.app.rss.feed.Feed.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.rss.feed.Feed getEntity() {
        return (io.skysail.server.app.rss.feed.Feed)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutFeedResourceGen.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FeedsResourceGen.class);
    }


}