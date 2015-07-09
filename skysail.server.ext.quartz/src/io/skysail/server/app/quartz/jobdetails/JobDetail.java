package io.skysail.server.app.quartz.jobdetails;

import org.quartz.JobDataMap;

public class JobDetail {

    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private JobDataMap jobDataMap;
    private boolean durable;

    public JobDetail(org.quartz.JobDetail jobDetail) {
        jobName = jobDetail.getKey().getName();
        jobGroup = jobDetail.getKey().getGroup();
        jobClassName = jobDetail.getJobClass().getName();
        jobDataMap = jobDetail.getJobDataMap();
        durable = jobDetail.isDurable();
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public boolean isDurable() {
        return durable;
    }
}
