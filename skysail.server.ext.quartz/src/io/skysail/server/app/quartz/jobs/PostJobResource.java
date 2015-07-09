package io.skysail.server.app.quartz.jobs;

import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.groups.PostGroupsResource;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostJobResource extends PostEntityServerResource<Job> {

	private QuartzApplication app;

	public PostJobResource() {
		addToContext(ResourceContextId.LINK_TITLE, "Create new Job");
		app = (QuartzApplication)getApplication();
	}

	@Override
	public Job createEntityTemplate() {
		return new Job();
	}
	
	@Override
	public SkysailResponse<?> addEntity(Job entity) {
		JobDetail jobDetail = org.quartz.JobBuilder.newJob(ConsoleTimePrinterJob.class)
				.withIdentity(entity.getName(), entity.getGroup())
				.storeDurably(true)
				.usingJobData("someProp", "someValue")
				.build();
		try {
	        app.getScheduler().addJob(jobDetail, true);
        } catch (SchedulerException e) {
	        e.printStackTrace();
        }
		return new SkysailResponse<String>();
	}
	
	@Override
	public List<Linkheader> getLinkheader() {
	    return super.getLinkheader(PostGroupsResource.class);
	}
	
	@Override
	public String redirectTo() {
	    return super.redirectTo(JobsResource.class);
	}

}
