package io.skysail.server.app.bb.goals.wc;

import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.app.bb.goals.PostAreaGoalsResource;

public class PostWorkAndCareerGoalResource extends PostAreaGoalsResource {

    @Override
    public DummyGoal createEntityTemplate() {
        return new DummyGoal("", Area.WORK_AND_CAREERS);
    }

}
