package io.skysail.server.app.plugins;

import io.skysail.api.links.Link;
import io.skysail.domain.Identifiable;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

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
