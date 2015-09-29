package io.skysail.server.app.bb.goals.f;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.bb.AreaGoalsResource;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.utils.LinkUtils;

public class FinanceGoalsResource extends AreaGoalsResource {
    
    public FinanceGoalsResource() {
        super(FinanceGoalResource.class, Area.FINANCE);
    }
    
    @Override
    public List<Link> getLinks() {
        List<Link> result = super.getLinks(app.getMainLinks());
        result.add(LinkUtils.fromResource(app, PostFinanceGoalsResource.class));
        return result;
    }


}
