package io.skysail.server.app.plugins;

import io.skysail.server.app.plugins.features.FeaturesResource;
import io.skysail.server.app.plugins.obr.PostResolverResource;
import io.skysail.server.app.plugins.obr.RepositoriesResource;
import io.skysail.server.app.plugins.resources.ResourcesResource;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PluginRootResource extends ListServerResource<String> {

    public PluginRootResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Plugins Management");
    }

    // @Override
    // public List<String> getData() {
    // return Collections.emptyList();
    // }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(FeaturesResource.class, RepositoriesResource.class, PostResolverResource.class,
                ResourcesResource.class);
    }

    @Override
    public List<String> getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
