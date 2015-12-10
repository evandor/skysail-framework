package io.skysail.server.app.quartz.groups.resources;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.quartz.QuartzRepository;
import io.skysail.server.app.quartz.groups.Group;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostGroupResource extends PostEntityServerResource<Group> {

	public PostGroupResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create New Group");
	}

	@Override
	public Group createEntityTemplate() {
		return new Group();
	}

	@Override
	public void addEntity(Group entity) {
		QuartzRepository.add(entity);
	}

	@Override
	public String redirectTo() {
	    return null;// super.redirectTo(JobsResource.class);
	}
}
