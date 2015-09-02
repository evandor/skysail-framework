package io.skysail.server.app.quartz.jobs;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.quartz.groups.resources.GroupsResource;
import io.skysail.server.app.quartz.schedules.PostScheduleResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class JobResource extends EntityServerResource<Job> {

	private String id;

	protected void doInit() throws ResourceException {
		id = getAttribute("id");
	}

	@Override
	public Job getEntity() {
		return null;//JobsRepository.getInstance().getById(id);
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
	public List<Link> getLinks() {
	    return super.getLinks(PostScheduleResource.class, GroupsResource.class);
	}

}
