package io.skysail.server.app.quartz.groups;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostGroupsResource extends PostEntityServerResource<Group> {

	public PostGroupsResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create New Group");
	}

	@Override
	public Group createEntityTemplate() {
		return new Group();
	}

	@Override
	public SkysailResponse<?> addEntity(Group entity) {
		GroupsRepository.getInstance().add(entity);
		return new SkysailResponse<String>();
	}

	@Override
	public String redirectTo() {
	    return null;// super.redirectTo(JobsResource.class);
	}
}
