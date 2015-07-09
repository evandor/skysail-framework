package io.skysail.server.app.quartz.jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import aQute.bnd.annotation.component.Component;

@Component
public class ConsoleTimePrinterJob implements org.quartz.Job {
	
	public ConsoleTimePrinterJob() {
		// Instances of Job must have a public no-argument constructor.
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getMergedJobDataMap();
		System.out.println("someProp = " + data.getString("someProp"));
	}
}
