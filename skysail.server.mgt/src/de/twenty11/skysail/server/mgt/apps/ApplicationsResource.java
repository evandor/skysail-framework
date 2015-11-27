package de.twenty11.skysail.server.mgt.apps;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;
import java.util.stream.Collectors;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.ManagementApplication;

public class ApplicationsResource extends ListServerResource<ApplicationDescriptor> {

    private ManagementApplication app;

    public ApplicationsResource() {
        super(ApplicationResource.class);
        app = (ManagementApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Applications");
    }

    @Override
    public List<ApplicationDescriptor> getEntity() {
        return app.getApplicationProviders().stream().map(app -> new ApplicationDescriptor(app))
                .collect(Collectors.toList());
    }

    public List<Link> getLinks() {
        return super.getLinks(ApplicationResource.class);
    };
}
