package io.skysail.server.ext.apt.test.twoentities.jobs;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.twoentities.jobs.*;


import de.twenty11.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.apt.test.twoentities.SchedulerGen;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ServerLink;

public class PostJobResource extends PostEntityServerResource<Job> {

    private String id;

	private SchedulerGen app;

	public PostJobResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Job");
    }

    @Override
	protected void doInit() throws ResourceException {
		app = (SchedulerGen)getApplication();
		id = getAttribute("id");
	}

	@Override
    public Job createEntityTemplate() {
	    return new Job();
    }

	@Override
    public SkysailResponse<?> addEntity(Job entity) {
		entity = JobsRepository.getInstance().add(entity);
	    return new SkysailResponse<String>();
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(JobsResource.class);
	}
}