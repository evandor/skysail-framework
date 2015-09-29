package io.skysail.server.app.bb.goals.wc;

import io.skysail.server.app.bb.goals.PutAreaGoalsResource;

public class PutWorkAndCareerGoalResource extends PutAreaGoalsResource {

    @Override
    public String redirectTo() {
        return super.redirectTo(WorkAndCareerGoalsResource.class);
    }
}
