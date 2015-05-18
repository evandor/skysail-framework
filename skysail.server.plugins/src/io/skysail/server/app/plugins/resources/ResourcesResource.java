package io.skysail.server.app.plugins.resources;

import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.app.plugins.obr.ObrResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ResourcesResource extends ListServerResource<ObrResource> {

    private String id;
    private PluginApplication app;

    public ResourcesResource() {
        super(ResourceResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Resources");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PluginApplication) getApplication();
    }

    @Override
    public List<ObrResource> getEntity() {
        return app.getResources(id);
    }
}
