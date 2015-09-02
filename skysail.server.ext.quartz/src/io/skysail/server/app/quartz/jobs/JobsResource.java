package io.skysail.server.app.quartz.jobs;

import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class JobsResource extends ListServerResource<Job> {

    private QuartzApplication app;

    public JobsResource() {
        super(JobResource.class);
        app = (QuartzApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "List Jobs");
    }

    @Override
    public List<Job> getEntity() {
        return app.getJobs();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(app.defaultResourcesPlus(PostJobResource.class));
    }
}
