package io.skysail.server.app.quartz.groups.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.quartz.QuartzRepository;
import io.skysail.server.app.quartz.groups.Group;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostGroupResource extends PostEntityServerResource<Group> {

	public PostGroupResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create New Group");
	}

	@Override
	public Group createEntityTemplate() {
		return new Group();
	}

	@Override
	public SkysailResponse<Group> addEntity(Group entity) {
		QuartzRepository.add(entity);
		return new SkysailResponse<>();
	}

	@Override
	public String redirectTo() {
	    return null;// super.redirectTo(JobsResource.class);
	}
}
