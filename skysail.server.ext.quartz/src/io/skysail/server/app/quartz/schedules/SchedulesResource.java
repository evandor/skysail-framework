package io.skysail.server.app.quartz.schedules;

import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.Job;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.quartz.Scheduler;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SchedulesResource extends ListServerResource<Job> {

	private QuartzApplication app;

	public SchedulesResource() {
		//super(SchedulesResource.class);
		app = (QuartzApplication) getApplication();
		addToContext(ResourceContextId.LINK_TITLE, "List Jobs");
	}

	@Override
    public List<Job> getEntity() {
		Scheduler scheduler = app.getScheduler();
//		try {
////			Function<? super String, ? extends Set<JobKey>> toJobKey = groupName -> {
////				try {
////					return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
////				} catch (Exception e) {
////					e.printStackTrace();
////					return null;
////				}
////			};
////			return scheduler.getJobGroupNames().stream().map(toJobKey).filter(jobKey -> {
////				return jobKey != null;
////			}).flatMap(jobKey -> jobKey.stream()).map(jobKey -> {
////				return new Job(null);
////			}).collect(Collectors.toList());
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//			return Collections.emptyList();
//		}
        return Collections.emptyList();
    }

}
