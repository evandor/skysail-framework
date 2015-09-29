package io.skysail.server.app.bb.goals.rf;

import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.app.bb.goals.PostAreaGoalsResource;

public class PostRecreationAndFreetimeGoalResource extends PostAreaGoalsResource {

    @Override
    public DummyGoal createEntityTemplate() {
        return new DummyGoal("", Area.RECREATION_AND_FREETIME);
    }


}
