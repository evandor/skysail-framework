package io.skysail.server.app.quartz.schedules;

import org.quartz.*;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostScheduleResource extends PostEntityServerResource<Schedule> {

    private String jobName;
    private String jobGroup;
    private QuartzApplication app;

    public PostScheduleResource() {
        app = (QuartzApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "schedule Job");
    }

    @Override
    protected void doInit() throws ResourceException {
        String id = getAttribute("id");
        jobName = id.split("-")[0];
        jobGroup = id.split("-")[1];
    }

    @Override
    public Schedule createEntityTemplate() {
        return new Schedule(jobName, jobGroup);
//        Scheduler scheduler = app.getScheduler();
//        try {
//            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupEquals(jobGroup));
//            Optional<JobKey> jobKey = jobKeys.stream().filter(key -> {
//                return key.getName().equals(jobName);
//            }).findFirst();
//            if (jobKey.isPresent()) {
//                JobDetail jobDetail = scheduler.getJobDetail(jobKey.get());
//                return new Schedule(jobDetail.get);
//            }
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//        throw new IllegalAccessError();
    }

    @Override
    public void addEntity(Schedule entity) {
        Trigger trigger = org.quartz.TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startNow()
                .forJob(JobKey.jobKey(entity.getJobName(), entity.getJobGroup()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(60).repeatForever()).build();
        try {
            app.getScheduler().scheduleJob(trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
