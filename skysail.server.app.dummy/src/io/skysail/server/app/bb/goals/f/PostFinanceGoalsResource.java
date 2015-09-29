package io.skysail.server.app.bb.goals.f;

import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.app.bb.goals.PostAreaGoalsResource;

public class PostFinanceGoalsResource extends PostAreaGoalsResource {

    @Override
    public DummyGoal createEntityTemplate() {
        return new DummyGoal("", Area.FINANCE);
    }

}
