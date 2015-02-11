package io.skysail.server.ext.apt.test.twoentities.schedules;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.twoentities.schedules.*;


import de.twenty11.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.apt.test.twoentities.SchedulerGen;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ServerLink;

public class PostScheduleResource extends PostEntityServerResource<Schedule> {

    private String id;

	private SchedulerGen app;

	public PostScheduleResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Schedule");
    }

    @Override
	protected void doInit() throws ResourceException {
		app = (SchedulerGen)getApplication();
		id = getAttribute("id");
	}

	@Override
    public Schedule createEntityTemplate() {
	    return new Schedule();
    }

	@Override
    public SkysailResponse<?> addEntity(Schedule entity) {
		entity = SchedulesRepository.getInstance().add(entity);
	    return new SkysailResponse<String>();
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(SchedulesResource.class);
	}
}