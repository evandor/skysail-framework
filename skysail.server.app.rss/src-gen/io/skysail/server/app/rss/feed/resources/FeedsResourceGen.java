package io.skysail.server.app.rss.feed.resources;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.rss.*;

import io.skysail.server.app.rss.feed.*;
import io.skysail.server.app.rss.feed.resources.*;


/**
 * generated from listResource.stg
 */
public class FeedsResourceGen extends ListServerResource<io.skysail.server.app.rss.feed.Feed> {

    private RSSApplication app;
    private FeedRepository repository;

    public FeedsResourceGen() {
        super(FeedResourceGen.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Feeds");
    }

    public FeedsResourceGen(Class<? extends FeedResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (RSSApplication) getApplication();
        repository = (FeedRepository) app.getRepository(io.skysail.server.app.rss.feed.Feed.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<io.skysail.server.app.rss.feed.Feed> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
              return super.getLinks(PostFeedResourceGen.class,FeedsResourceGen.class);
    }
}