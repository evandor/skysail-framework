package io.skysail.server.app.quartz.groups;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class GroupsResource extends ListServerResource<Group> {

	public GroupsResource() {
		super(GroupResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "List Groups");
	}

	@Override
	public List<Group> getData() {
		return GroupsRepository.getInstance().getGroups();

	}
	
	@Override
	public List<Linkheader> getLinkheader() {
	    return super.getLinkheader(PostGroupsResource.class);
	}
}
