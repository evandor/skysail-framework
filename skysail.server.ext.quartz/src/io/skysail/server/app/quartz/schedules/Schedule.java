package io.skysail.server.app.quartz.schedules;

import io.skysail.api.forms.Field;
import io.skysail.server.app.quartz.jobs.Job;

import org.quartz.JobDetail;

public class Schedule {

	private Job job;
    private JobDetail jobDetail;
    private String jobName;
    private String jobGroup;

	public Schedule(Job job) {
		this.job = job;
    }

	public Schedule(String jobName, String jobGroup) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
    }

    public Schedule() {
        // TODO Auto-generated constructor stub
    }

    @Field
	private String trigger;

	public String getTrigger() {
	    return trigger;
    }

	public void setTrigger(String trigger) {
	    this.trigger = trigger;
    }

	public String getJobName() {
        return jobName;
    }

	public String getJobGroup() {
        return jobGroup;
    }

}
