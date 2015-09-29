package io.skysail.server.app.bb.goals.wc;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.bb.AreaGoalResource;
import io.skysail.server.app.bb.achievements.PostAchievementResource;

public class WorkAndCareerGoalResource extends AreaGoalResource {    
   
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutWorkAndCareerGoalResource.class, PostAchievementResource.class);
    }

}
