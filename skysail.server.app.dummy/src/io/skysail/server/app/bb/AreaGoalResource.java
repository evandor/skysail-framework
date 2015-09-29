package io.skysail.server.app.bb;

import org.restlet.resource.ResourceException;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bb.areas.Area;
import io.skysail.server.restlet.resources.EntityServerResource;

public abstract class AreaGoalResource extends EntityServerResource<DummyGoal> {

    protected BBApplication app;

    protected Area area;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (BBApplication) getApplication();
    }
    
    @Override
    public DummyGoal getEntity() {
        return app.getRepository().getById(getAttribute("id"));
    }

    
    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
