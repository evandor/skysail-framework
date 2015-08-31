package io.skysail.server.app.quartz.triggers;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.JobsResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostTriggerResource extends PostEntityServerResource<Trigger> {

	private QuartzApplication app;

    public PostTriggerResource() {
	    app = (QuartzApplication)getApplication();
		addToContext(ResourceContextId.LINK_TITLE, "Create new Trigger");
	}

	@Override
	public Trigger createEntityTemplate() {
		return new Trigger();
	}

	@Override
	public SkysailResponse<?> addEntity(Trigger entity) {
        org.quartz.Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
                .withIdentity(entity.getName())
                .build();
//		app.getScheduler().
        return new SkysailResponse<String>();
	}

	@Override
	public String redirectTo() {
	    return super.redirectTo(JobsResource.class);
	}

}
