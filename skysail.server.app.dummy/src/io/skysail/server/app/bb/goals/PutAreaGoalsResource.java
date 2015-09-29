package io.skysail.server.app.bb.goals;

import org.restlet.resource.ResourceException;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.bb.BBApplication;
import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutAreaGoalsResource extends PutEntityServerResource<DummyGoal> {

	private BBApplication app;

	@Override
	protected void doInit() throws ResourceException {
		super.doInit();
		app = (BBApplication)getApplication();
	}

    @Override
    public DummyGoal getEntity() {
        return app.getRepository().getById(getAttribute("id"));
    }
    
    public SkysailResponse<DummyGoal> updateEntity(DummyGoal entityFromTheWire) {
        DummyGoal entityToBeUpdated = getEntity(null);
        SkysailApplication app = (SkysailApplication)getApplication();
        copyProperties(entityToBeUpdated,entityFromTheWire);

        app.getRepository().update(getAttribute("id"), entityToBeUpdated);
        return new SkysailResponse<>();
    }
	
}