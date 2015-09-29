package io.skysail.server.app.bb.goals.rf;

import io.skysail.server.app.bb.goals.PutAreaGoalsResource;

public class PutRecreationAndFreetimeGoalResource extends PutAreaGoalsResource {

    @Override
    public String redirectTo() {
        return super.redirectTo(RecreationAndFreetimeGoalsResource.class);
    }
}
