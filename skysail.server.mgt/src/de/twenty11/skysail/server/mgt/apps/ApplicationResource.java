package de.twenty11.skysail.server.mgt.apps;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

import de.twenty11.skysail.server.mgt.ManagementApplication;

public class ApplicationResource extends EntityServerResource<ApplicationDescriptor> {

    private ManagementApplication app;

    public ApplicationResource() {
        app = (ManagementApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Application");
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        throw new IllegalStateException("operation not supported");
    }

    @Override
    public ApplicationDescriptor getEntity() {
        Optional<ApplicationDescriptor> findFirst = app.getApplicationProviders().stream()
            .map(app -> new ApplicationDescriptor(app))
            .filter(descriptor -> descriptor.getName().equals(getAttribute("id")))
            .findFirst();
        return findFirst.orElse(new ApplicationDescriptor("not found"));
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ApplicationResource.class);
    }

}
