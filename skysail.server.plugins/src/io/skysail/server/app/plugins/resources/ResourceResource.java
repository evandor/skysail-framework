package io.skysail.server.app.plugins.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.app.plugins.obr.ObrResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class ResourceResource extends EntityServerResource<ObrResource> {

    private String symbolicName;
    private String version;
    private PluginApplication app;

    public ResourceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Details");
    }

    @Override
    protected void doInit() throws ResourceException {
        String identifier = getAttribute("id");
        if (identifier == null) {
            return;
        }
        String[] split = identifier.split(";");
        symbolicName = split[0];
        version = split[1];
        app = (PluginApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ResourceResource.class);
    }

    @Override
    public ObrResource getEntity() {
        return null;
    }
}
