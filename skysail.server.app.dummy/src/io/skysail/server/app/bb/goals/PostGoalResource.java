//package io.skysail.server.app.bb.goals;
//
//import org.restlet.resource.ResourceException;
//
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.bb.BBApplication;
//import io.skysail.server.app.bb.DummyGoal;
//import io.skysail.server.restlet.resources.PostEntityServerResource;
//
//public class PostGoalResource extends PostEntityServerResource<DummyGoal> {
//
//	private BBApplication app;
//
//	@Override
//	protected void doInit() throws ResourceException {
//		super.doInit();
//		app = (BBApplication)getApplication();
//	}
//	
//	@Override
//	public DummyGoal createEntityTemplate() {
//		return new DummyGoal();
//	}
//
//	@Override
//	public SkysailResponse<DummyGoal> addEntity(DummyGoal entity) {
//		String id = app.getRepository().save(entity).toString();
//		entity.setId(id);
//		return new SkysailResponse<>(entity);
//	}
//
////	@Override
////	public String redirectTo() {
////	    return super.redirectTo(GoalsResource.class);
////	}
//}