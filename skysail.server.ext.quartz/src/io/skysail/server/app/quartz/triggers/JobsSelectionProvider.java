package io.skysail.server.app.quartz.triggers;

import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.Job;

import java.util.*;

import org.restlet.resource.Resource;

import aQute.bnd.annotation.component.*;

@Component(immediate = true)
public class JobsSelectionProvider implements io.skysail.api.forms.SelectionProvider {

    private static JobsSelectionProvider instance;

    private Resource resource;

    public static JobsSelectionProvider getInstance() {
        return JobsSelectionProvider.instance;
    }

    @Activate
    public void activate() {
        JobsSelectionProvider.instance = this;
    }

    @Deactivate
    public void deactivate() {
        JobsSelectionProvider.instance = null;
    }

    @Override
    public Map<String, String> getSelections() {
        List<Job> jobs = ((QuartzApplication)resource.getApplication()).getJobs();
        Map<String,String> result = new HashMap<>();
        jobs.stream().forEach(j -> result.put(j.getId().toString(), j.getName()));
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
