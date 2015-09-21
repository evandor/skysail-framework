package io.skysail.server.app.twitter4j.resources;

import io.skysail.server.app.twitter4j.TwitterApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import twitter4j.*;

@Slf4j
public class TimelineResource extends ListServerResource<Tweet>{

    public TimelineResource() {
        super(TimelineEntityResource.class);
    }

    @Override
    public List<Tweet> getEntity() {
        List<Tweet> tweets = new ArrayList<>();
        Twitter twitter = ((TwitterApplication) getApplication()).getTwitterInstance();
        List<Status> statuses = new ArrayList<>();
        try {
            statuses = twitter.getHomeTimeline();
            for (Status status : statuses) {
                tweets.add(new Tweet(status));
            }
        } catch (TwitterException e) {
            log.error(e.getMessage(), e);
        }

        return tweets;
    }

//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(SearchRequestResource.class);
//    }

}
