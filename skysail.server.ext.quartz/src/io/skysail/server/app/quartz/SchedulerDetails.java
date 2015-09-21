package io.skysail.server.app.quartz;

import io.skysail.api.domain.Identifiable;

import org.quartz.*;

public class SchedulerDetails implements Identifiable {

    private SchedulerMetaData metaData;

    public SchedulerDetails(Scheduler scheduler) {
        try {
            metaData = scheduler.getMetaData();
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getName() {
        return metaData.getSchedulerName();
    }

    public int getJobsExecuted() {
        return metaData.getNumberOfJobsExecuted();
    }

    public String getSummary() {
        try {
            return metaData.getSummary();
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }


}
