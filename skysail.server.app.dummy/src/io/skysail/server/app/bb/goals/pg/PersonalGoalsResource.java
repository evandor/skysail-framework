package io.skysail.server.app.bb.goals.pg;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.bb.AreaGoalsResource;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.utils.LinkUtils;

public class PersonalGoalsResource extends AreaGoalsResource {
    
    public PersonalGoalsResource() {
        super(PersonalGoalResource.class, Area.PERSONAL_GOALS);
    }
    
    @Override
    public List<Link> getLinks() {
        List<Link> result = super.getLinks(app.getMainLinks());
        result.add(LinkUtils.fromResource(app, PostPersonalGoalsResource.class));
        return result;
    }


}
