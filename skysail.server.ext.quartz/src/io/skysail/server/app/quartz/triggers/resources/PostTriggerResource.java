package io.skysail.server.app.quartz.triggers.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.*;
import io.skysail.server.app.quartz.triggers.Trigger;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.List;

import org.quartz.*;

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
        org.quartz.Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(entity.getName())
                .startNow()
                .withSchedule(org.quartz.SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(40)
                .repeatForever())
                .build();

        JobDetail jobDetail = org.quartz.JobBuilder.newJob(ConsoleTimePrinterJob.class)
                .withIdentity("myJob")
                //.storeDurably(true)
                .usingJobData("someProp", "someValue")
                .build();

//        app.getJobs().stream().filter(j -> { return j.getId().equals(jobId);}).findFirst().ifPresent(j -> {
//            app.getScheduler().scheduleJob(j., trigger);
//        });

        try {
            app.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new SkysailResponse<String>();
	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(app.defaultResourcesPlus());
	}

	@Override
	public String redirectTo() {
	    return super.redirectTo(JobsResource.class);
	}

}
