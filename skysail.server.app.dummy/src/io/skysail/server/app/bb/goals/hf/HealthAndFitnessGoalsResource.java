package io.skysail.server.app.bb.goals.hf;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.bb.AreaGoalsResource;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.utils.LinkUtils;

public class HealthAndFitnessGoalsResource extends AreaGoalsResource {
    
    public HealthAndFitnessGoalsResource() {
        super(HealthAndFitnessGoalResource.class, Area.HEALTH_AND_FITNESS);
    }
    
    @Override
    public List<Link> getLinks() {
        List<Link> result = super.getLinks(app.getMainLinks());
        result.add(LinkUtils.fromResource(app, PostHealthAndFitnessGoalsResource.class));
        return result;
    }


}
