package io.skysail.server.app.twitter4j.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.twitter4j.TwitterApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.ResourceException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TimelineResource extends ListServerResource<Tweet>{

    private TwitterApplication app;

    @Override
    protected void doInit() throws ResourceException {
        app = (TwitterApplication) getApplication();
    }
    
    @Override
    public List<Tweet> getEntity() {
        List<Tweet> tweets = new ArrayList<>();
        Twitter twitter = app.getTwitterInstance();
        List<Status> statuses;
        try {
            statuses = twitter.getHomeTimeline();
            //System.out.println("Showing home timeline.");
            for (Status status : statuses) {
                //System.out.println(status.getUser().getName() + ":" + status.getText());
                tweets.add(new Tweet(status));
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }
       
        return tweets;
    }
    
    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(SearchRequestResource.class);
    }

}
