package io.skysail.server.app.rss.feed.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.rss.*;
import io.skysail.server.app.rss.feed.*;

/**
 * generated from putResource.stg
 */
public class PutFeedResourceGen extends PutEntityServerResource<io.skysail.server.app.rss.feed.Feed> {


    protected String id;
    protected RSSApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (RSSApplication)getApplication();
    }

    @Override
    public void updateEntity(Feed  entity) {
        io.skysail.server.app.rss.feed.Feed original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.rss.feed.Feed.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.rss.feed.Feed getEntity() {
        return (io.skysail.server.app.rss.feed.Feed)app.getRepository(io.skysail.server.app.rss.feed.Feed.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FeedsResourceGen.class);
    }
}