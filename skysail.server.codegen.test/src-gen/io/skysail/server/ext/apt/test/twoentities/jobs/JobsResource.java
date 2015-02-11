package io.skysail.server.ext.apt.test.twoentities.jobs;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.twoentities.jobs.*;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class JobsResource extends ListServerResource<Job> {

    private String id;

    public JobsResource() {
        super(JobResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Jobs");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public List<Job> getData() {
        return JobsRepository.getInstance().getJobs();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostJobResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> { l.substitute("id", id); };
    }
}
