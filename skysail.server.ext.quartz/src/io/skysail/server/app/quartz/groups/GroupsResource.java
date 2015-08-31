package io.skysail.server.app.quartz.groups;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class GroupsResource extends ListServerResource<Group> {

	public GroupsResource() {
		super(GroupResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "List Groups");
	}

	@Override
	public List<Group> getEntity() {
		return GroupsRepository.getInstance().getGroups();

	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(PostGroupsResource.class);
	}
}
