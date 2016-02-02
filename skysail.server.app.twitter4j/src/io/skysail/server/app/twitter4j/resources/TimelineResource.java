package io.skysail.server.app.twitter4j.resources;

import java.util.ArrayList;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.links.LinkRelation;
import io.skysail.server.app.twitter4j.TwitterApplication;
import io.skysail.server.restlet.resources.ListServerResource;
import lombok.extern.slf4j.Slf4j;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

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

    @Override
    public List<Link> getLinks() {
        List<Link> result = new ArrayList<>();
        result.add(new Link.Builder("v1/facebook").title("Facebook").relation(LinkRelation.COLLECTION).build());
        result.add(new Link.Builder("v1/github").title("Github").relation(LinkRelation.COLLECTION).build());
        return result;
    }

}
