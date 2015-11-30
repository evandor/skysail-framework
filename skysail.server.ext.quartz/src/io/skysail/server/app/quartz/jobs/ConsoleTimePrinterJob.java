package io.skysail.server.app.quartz.jobs;

import org.osgi.service.component.annotations.Component;
import org.quartz.*;

@Component(immediate = true)
public class ConsoleTimePrinterJob implements org.quartz.Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getMergedJobDataMap();
		System.out.println("someProp = " + data.getString("someProp"));
	}
}
