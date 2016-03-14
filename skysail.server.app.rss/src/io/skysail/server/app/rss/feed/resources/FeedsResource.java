package io.skysail.server.app.rss.feed.resources;

import io.skysail.server.ResourceContextId;

public class FeedsResource extends FeedsResourceGen {

    public FeedsResource() {
        super(FeedResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Feeds");
    }
}
