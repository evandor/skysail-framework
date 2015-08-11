package io.skysail.server.app.quartz.jobs;

import io.skysail.server.app.quartz.groups.GroupsResource;
import io.skysail.server.app.quartz.schedules.PostScheduleResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class JobResource extends EntityServerResource<Job> {

	private String id;

	protected void doInit() throws ResourceException {
		id = getAttribute("id");
	}

	@Override
	public Job getData() {
		return JobsRepository.getInstance().getById(id);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}
	
	@Override
	public List<Linkheader> getLinkheader() {
	    return super.getLinkheader(PostScheduleResource.class, GroupsResource.class);
	}

}