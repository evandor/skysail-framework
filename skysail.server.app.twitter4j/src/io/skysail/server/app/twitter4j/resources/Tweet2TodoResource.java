package io.skysail.server.app.twitter4j.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.twitter4j.TwitterApplication;
import io.skysail.server.restlet.resources.EntityServerResource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Reference;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@Getter
@Slf4j
public class Tweet2TodoResource extends EntityServerResource<Tweet> {

    private String id;

    @Override
    protected void doInit() {
        id = getAttribute("id");
    }
    
    @Override
    public SkysailResponse<?> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tweet getEntity() {
        Twitter twitter = ((TwitterApplication) getApplication()).getTwitterInstance();
        try {
            Status status = twitter.showStatus(Long.parseLong(id));
            Tweet tweet = new Tweet(status);
            String title = Reference.encode("Todo from Tweet");
            String desc = Reference.encode(tweet.getText());
            String uri = "/Todos/Lists/list:null/Todos/?title="+title+"&desc=" + desc;
            getResponse().redirectSeeOther(uri);
            return tweet;
        } catch (NumberFormatException | TwitterException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
