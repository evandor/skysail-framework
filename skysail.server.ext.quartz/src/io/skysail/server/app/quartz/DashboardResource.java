package io.skysail.server.app.quartz;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.quartz.Scheduler;

public class DashboardResource extends ListServerResource<SchedulerDetails> {

    private QuartzApplication app;

    public DashboardResource() {
        app = (QuartzApplication) getApplication();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(app.defaultResourcesPlus());
    }

    @Override
    public List<SchedulerDetails> getEntity() {
        Scheduler scheduler = app.getScheduler();
        SchedulerDetails details = new SchedulerDetails(scheduler);
        return Arrays.asList(details);
    }
}
