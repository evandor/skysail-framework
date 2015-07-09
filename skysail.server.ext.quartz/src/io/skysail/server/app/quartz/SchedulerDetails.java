package io.skysail.server.app.quartz;

import org.quartz.*;

public class SchedulerDetails {

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


}
