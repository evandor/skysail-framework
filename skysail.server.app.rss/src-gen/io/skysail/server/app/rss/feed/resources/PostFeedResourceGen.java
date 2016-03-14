package io.skysail.server.app.rss.feed.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.rss.*;
import io.skysail.server.app.rss.feed.*;

/**
 * generated from postResource.stg
 */
public class PostFeedResourceGen extends PostEntityServerResource<io.skysail.server.app.rss.feed.Feed> {

	protected RSSApplication app;

    public PostFeedResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (RSSApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.rss.feed.Feed createEntityTemplate() {
        return new Feed();
    }

    @Override
    public void addEntity(io.skysail.server.app.rss.feed.Feed entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.rss.feed.Feed.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FeedsResourceGen.class);
    }
}