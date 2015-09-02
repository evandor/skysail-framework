package io.skysail.server.app.quartz.jobdetails;

import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.PostJobResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class JobDetailsResource extends ListServerResource<JobDetail> {

    private QuartzApplication app;

    public JobDetailsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "List Job Details");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (QuartzApplication)getApplication();
    }

    @Override
    public List<JobDetail> getEntity() {
        Scheduler scheduler = app.getScheduler();
        try {
            Function<? super String, ? extends Set<JobKey>> toJobKey = groupName -> {
                try {
                    return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };
            return scheduler.getJobGroupNames().stream().map(toJobKey).filter(jobKey -> {
                return jobKey != null;
            }).flatMap(jobKey -> jobKey.stream()).map(jobKey -> {
                try {
                    return new JobDetail(scheduler.getJobDetail(jobKey));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostJobResource.class);
    }

}
