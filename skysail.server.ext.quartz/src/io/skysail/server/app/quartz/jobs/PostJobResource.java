package io.skysail.server.app.quartz.jobs;

import java.util.List;

import org.quartz.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.groups.resources.GroupsResource;
import io.skysail.server.app.quartz.jobdetails.JobDetailsResource;
import io.skysail.server.app.quartz.triggers.resources.TriggersResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;

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
	public void addEntity(Job entity) {
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
	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(GroupsResource.class, JobDetailsResource.class, TriggersResource.class);
	}

	@Override
	public String redirectTo() {
	    return super.redirectTo(JobsResource.class);
	}

}
