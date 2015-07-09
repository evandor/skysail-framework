package io.skysail.server.app.quartz.jobs;

import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.groups.GroupsResource;
import io.skysail.server.app.quartz.schedules.SchedulesResource;
import io.skysail.server.app.quartz.triggers.TriggersResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class JobsResource extends ListServerResource<Job> {

	private QuartzApplication app;

	public JobsResource() {
		super(JobResource.class);
		app = (QuartzApplication) getApplication();
		addToContext(ResourceContextId.LINK_TITLE, "List Jobs");
	}

	@Override
	public List<Job> getData() {
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
	public List<Linkheader> getLinkheader() {
	    return super.getLinkheader(TriggersResource.class, PostJobResource.class, SchedulesResource.class, GroupsResource.class);
	}
}
