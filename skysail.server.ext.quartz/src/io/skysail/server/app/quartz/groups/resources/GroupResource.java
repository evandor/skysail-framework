package io.skysail.server.app.quartz.groups.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.groups.Group;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class GroupResource extends EntityServerResource<Group> {

	private String id;

    private QuartzApplication app;

	public GroupResource() {
	    app = (QuartzApplication) getApplication();
    }

	protected void doInit() throws ResourceException {
		id = getAttribute("id");
	}

	@Override
	public Group getEntity() {
		return app.getRepository().getById(Group.class, id);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		app.getRepository().delete(Group.class, id);
		return new SkysailResponse<String>();
	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(PutGroupResource.class);
	}

}
