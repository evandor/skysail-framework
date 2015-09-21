package io.skysail.server.app.plugins.installations;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.plugins.features.Feature;

public class Installation implements Identifiable {

    private Feature feature;

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
