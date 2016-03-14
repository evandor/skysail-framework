package io.skysail.server.app.rss.feed.resources;

import java.util.List;

import io.skysail.api.links.Link;

/**
 * generated from entityResource.stg
 */
public class FeedResource extends FeedResourceGen {


    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutFeedResourceGen.class, FeedContentsResource.class);
    }
}