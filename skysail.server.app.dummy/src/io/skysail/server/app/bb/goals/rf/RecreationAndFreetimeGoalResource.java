package io.skysail.server.app.bb.goals.rf;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.bb.AreaGoalResource;

public class RecreationAndFreetimeGoalResource extends AreaGoalResource {    
   
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutRecreationAndFreetimeGoalResource.class);
    }

}
