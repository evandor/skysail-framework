package io.skysail.server.app.plugins;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PluginRootResource extends ListServerResource<Identifiable> {

    private PluginApplication app;

    public PluginRootResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Plugins Management");
        app = (PluginApplication)getApplication();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(app.getMainLinks());
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

}
