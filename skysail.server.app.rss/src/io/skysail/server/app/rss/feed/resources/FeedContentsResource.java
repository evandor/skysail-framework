package io.skysail.server.app.rss.feed.resources;

import java.net.URL;
import java.util.*;

import com.rometools.rome.feed.synd.*;

import io.skysail.server.app.rss.*;
import io.skysail.server.app.rss.feed.Feed;
import io.skysail.server.restlet.resources.ListServerResource;

public class FeedContentsResource extends ListServerResource<FeedEntry> {
    
    private RSSApplication app;
    private FeedRepository repository;

    @Override
    protected void doInit() {
        app = (RSSApplication) getApplication();
        repository = (FeedRepository) app.getRepository(io.skysail.server.app.rss.feed.Feed.class);
    }

    @Override
    public List<?> getEntity() {
        List<FeedEntry> result = new ArrayList<>();
        Feed feed = repository.findOne(getAttribute("id"));
        try {
            SyndFeed feed2 = app.getRssClient().getFeed(new URL(feed.getUrl()));
            for (SyndEntry e : feed2.getEntries()) {
                result.add(new FeedEntry(e));
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

}
