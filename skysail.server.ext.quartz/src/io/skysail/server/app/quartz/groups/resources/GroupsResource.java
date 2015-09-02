package io.skysail.server.app.quartz.groups.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.groups.Group;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class GroupsResource extends ListServerResource<Group> {

	private QuartzApplication app;

    public GroupsResource() {
		super(GroupResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "List Groups");
	}

	@Override
	protected void doInit() throws ResourceException {
	    super.doInit();
	    app = (QuartzApplication)getApplication();
	}

	@Override
	public List<Group> getEntity() {
	    Filter filter = new Filter(getRequest());
        //filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        //filter.addEdgeOut("parent", "#" + listId);

        Pagination pagination = new Pagination(getRequest(), getResponse(), app.getRepository().getCount(Group.class, filter));
		return app.getRepository().findAll(Group.class, filter, pagination);

	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(app.defaultResourcesPlus(PostGroupResource.class));
	}
}
