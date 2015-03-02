package io.skysail.server.app.plugins.features;

import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.app.plugins.PluginRootResource;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class FeaturesResource extends ListServerResource<Feature> {

    private PluginApplication app;

    public FeaturesResource() {
        super(FeatureResource.class);
        app = (PluginApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Features");
    }

    // @Override
    // public List<Feature> getData() {
    // return app.getFeaturesRepository().getFeatures();
    // }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PluginRootResource.class);
    }

}
