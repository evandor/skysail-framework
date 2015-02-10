package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.features.Feature;
import io.skysail.api.features.FeatureContext;

public enum SecurityFeatures implements Feature {

    ALLOW_ORIGIN_FEATURE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}