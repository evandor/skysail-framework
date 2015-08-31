package io.skysail.server.app.quartz;

import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.groups.GroupsResource;
import io.skysail.server.app.quartz.jobdetails.JobDetailsResource;
import io.skysail.server.app.quartz.triggers.TriggersResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.quartz.Scheduler;


public class DashboardResource extends ListServerResource<SchedulerDetails> {

	private QuartzApplication app;

	public DashboardResource() {
	 app = (QuartzApplication)getApplication();
    }

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(GroupsResource.class, JobDetailsResource.class, TriggersResource.class);
	}

    @Override
    public List<SchedulerDetails> getEntity() {
        Scheduler scheduler = app.getScheduler();
        SchedulerDetails details = new SchedulerDetails(scheduler);
        return Arrays.asList(details);
    }
}
