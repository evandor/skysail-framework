package io.skysail.server.app.plugins.features;

import io.skysail.api.links.Link;
import io.skysail.server.app.plugins.*;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class FeaturesResource extends ListServerResource<Feature> {

    private PluginApplication app;

    public FeaturesResource() {
        super(FeatureResource.class);
        app = (PluginApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Features");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PluginRootResource.class);
    }

    @Override
    public List<Feature> getEntity() {
        return null;
    }

}
