package io.skysail.server.app.bb.goals.pg;

import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.app.bb.goals.PostAreaGoalsResource;

public class PostPersonalGoalsResource extends PostAreaGoalsResource {

    @Override
    public DummyGoal createEntityTemplate() {
        return new DummyGoal("", Area.PERSONAL_GOALS);
    }

}
