package io.skysail.server.app.plugins;

import io.skysail.api.links.Link;
import io.skysail.server.app.plugins.features.FeaturesResource;
import io.skysail.server.app.plugins.obr.PostResolverResource;
import io.skysail.server.app.plugins.obr.RepositoriesResource;
import io.skysail.server.app.plugins.resources.ResourcesResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PluginRootResource extends ListServerResource<String> {

    public PluginRootResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Plugins Management");
    }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(FeaturesResource.class, RepositoriesResource.class, PostResolverResource.class,
                ResourcesResource.class);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

}
