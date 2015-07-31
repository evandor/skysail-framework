package io.skysail.server.app.quartz.groups;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class GroupResource extends EntityServerResource<Group> {

	private String id;

	protected void doInit() throws ResourceException {
		id = getAttribute("id");
	}

	@Override
	public Group getData() {
		return GroupsRepository.getInstance().getById(id);
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
	    return super.getLinks(PutGroupResource.class);
	}

}
