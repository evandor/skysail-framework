package io.skysail.server.app.twitter4j.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

public class TimelineEntityResource extends EntityServerResource<Tweet> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Tweet getEntity() {
        return new Tweet();
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(Tweet2TodoResource.class);
    }

}
