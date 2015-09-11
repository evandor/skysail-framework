package io.skysail.server.app.twitter4j.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.twitter4j.TwitterApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.restlet.data.Form;

import twitter4j.*;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SearchRequestResource extends PostEntityServerResource<String> {

    private TwitterApplication app;

    public SearchRequestResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Search Twitter");
    }

    public void doInit() {
        app = (TwitterApplication) getApplication();
     }


    @Override
    public String createEntityTemplate() {
        return "";
    }

    public String getData(Form form) {
        return "source:twitter4j yusukey";//populate(createEntityTemplate(), form);
    }


    @Override
    public SkysailResponse<String> addEntity(String entity) {
        Query query = new Query("source:twitter4j yusukey");
        QueryResult result;
        try {
            result = app.getTwitterInstance().search(query);
            for (Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            }
        } catch (TwitterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       return new SkysailResponse<String>("hi");
    }

}
