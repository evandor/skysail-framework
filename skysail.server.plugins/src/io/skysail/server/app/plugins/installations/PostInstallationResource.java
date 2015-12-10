package io.skysail.server.app.plugins.installations;

import java.util.Optional;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.app.plugins.features.Feature;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostInstallationResource extends PostEntityServerResource<Installation> {

    private PluginApplication app;

    public PostInstallationResource() {
        app = (PluginApplication) getApplication();
    }

    private String featureId;

    @Override
    protected void doInit() throws ResourceException {
        featureId = (String) getRequest().getAttributes().get("id");
    }

    public Installation createEntity() {
        return new Installation();
    }
    
    @Override
    public Installation createEntityTemplate() {
        return getData(null);
    }

    public Installation getData(Form form) {
        Installation note;
        note = new Installation();
        Optional<Feature> feature = app.getFeaturesRepository().getFeature(featureId);
        note.setFeature(feature.orElse(null));
        // note.setOwner(app.getCurrentUser().getId());
        return note;
    }

    public void addEntity(Installation installation) {
        app.install(installation.getFeature());
    }
}
