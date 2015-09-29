//package io.skysail.server.app.bb.goals;
//
//import java.util.List;
//
//import org.restlet.resource.ResourceException;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.app.bb.BodyboosterApplication;
//import io.skysail.server.app.bb.DummyGoal;
//import io.skysail.server.app.bb.areas.Area;
//import io.skysail.server.queryfilter.Filter;
//import io.skysail.server.restlet.resources.ListServerResource;
//
//public class GoalsResource extends ListServerResource<DummyGoal> {
//
//	private BodyboosterApplication app;
//
//	public GoalsResource() {
//        super(GoalResource.class);
//    }
//	
//	@Override
//	protected void doInit() throws ResourceException {
//		super.doInit();
//		app = (BodyboosterApplication)getApplication();
//	}
//
//	@Override
//	public List<DummyGoal> getEntity() {
//		Filter filter = new Filter(getRequest());
//		filter.add("area", Area.WORK_AND_CAREERS.name());
//        return app.getRepository().find(filter);
//	}
//	
//	@Override
//	public List<Link> getLinks() {
//	    return super.getLinks(app.getMainLinks());
//	}
//
//}
