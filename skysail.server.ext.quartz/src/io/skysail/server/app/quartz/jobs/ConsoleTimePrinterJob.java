package io.skysail.server.app.quartz.jobs;

import org.quartz.*;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true)
public class ConsoleTimePrinterJob implements org.quartz.Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getMergedJobDataMap();
		System.out.println("someProp = " + data.getString("someProp"));
	}
}
