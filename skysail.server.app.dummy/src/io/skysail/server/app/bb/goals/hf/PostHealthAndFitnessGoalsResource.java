package io.skysail.server.app.bb.goals.hf;

import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.app.bb.goals.PostAreaGoalsResource;

public class PostHealthAndFitnessGoalsResource extends PostAreaGoalsResource {

    @Override
    public DummyGoal createEntityTemplate() {
        return new DummyGoal("", Area.HEALTH_AND_FITNESS);
    }

}
