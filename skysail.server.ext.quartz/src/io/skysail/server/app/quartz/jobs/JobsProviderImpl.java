package io.skysail.server.app.quartz.jobs;

import java.util.*;

import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.*;

@Component(immediate = true)
public class JobsProviderImpl implements io.skysail.api.forms.SelectionProvider {

    private static JobsProviderImpl instance;
    private volatile List<org.quartz.Job> jobs = new ArrayList<>();

    public static JobsProviderImpl getInstance() {
        return JobsProviderImpl.instance;
    }

    @Activate
    public void activate() {
        JobsProviderImpl.instance = this;
    }

    @Deactivate
    public void deactivate() {
        JobsProviderImpl.instance = null;
    }

   // @Reference(multiple = true, optional = false, dynamic = true)
    public void addJob(org.quartz.Job job) {
        jobs.add(job);
    }

    public void removeJob(org.quartz.Job job) {
        jobs.remove(job);
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<String, String>();
        jobs.stream().forEach(job -> result.put(job.getClass().getName(), job.getClass().getSimpleName()));
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }


}
