package io.skysail.server.app.quartz.groups;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.restlet.resource.ResourceException;

public class PutGroupResource extends PutEntityServerResource<Group> {

	private QuartzApplication app;
	private String id;

	public PutGroupResource() {
        app = (QuartzApplication) getApplication();
     }

     protected void doInit() throws ResourceException {
       id = getAttribute("id");
     }

 	@Override
     public Group getEntity() {
        return GroupsRepository.getInstance().getById(id);
     }


	@Override
	public SkysailResponse<?> updateEntity(Group entity) {
		GroupsRepository.getInstance().update(entity);
		return new SkysailResponse<String>();
	}

	@Override
	public String redirectTo() {
	    return null;//super.redirectTo(JobsResource.class);
	}
}
