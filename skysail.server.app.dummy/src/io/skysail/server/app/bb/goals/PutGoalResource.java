package io.skysail.server.app.bb.goals;

import org.restlet.resource.ResourceException;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.bb.BBApplication;
import io.skysail.server.app.bb.DummyGoal;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutGoalResource extends PutEntityServerResource<DummyGoal> {

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

	@Override
	public SkysailResponse<DummyGoal> updateEntity(DummyGoal entity) {
		Object update = app.getRepository().update(getAttribute("id"), entity);
		return new SkysailResponse<>();
	}
	
//	@Override
//	public String redirectTo() {
//	    return super.redirectTo(GoalsResource.class);
//	}


}
