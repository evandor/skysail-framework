package io.skysail.server.app.quartz.jobs;

import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.groups.GroupsResource;
import io.skysail.server.app.quartz.schedules.SchedulesResource;
import io.skysail.server.app.quartz.triggers.TriggersResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class JobsResource extends ListServerResource<Job> {

	private QuartzApplication app;

	public JobsResource() {
		super(JobResource.class);
		app = (QuartzApplication) getApplication();
		addToContext(ResourceContextId.LINK_TITLE, "List Jobs");
	}

	@Override
	public List<Job> getEntity() {
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
	                return new Job(jobKey);
	            }).collect(Collectors.toList());
	      } catch (SchedulerException e) {
	          e.printStackTrace();
	          return Collections.emptyList();
	      }
	}

	@Override
	public List<Link> getLinks() {
	    return super.getLinks(TriggersResource.class, PostJobResource.class, SchedulesResource.class, GroupsResource.class);
	}
}
