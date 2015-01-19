package de.twenty11.skysail.server.core.restlet;

import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.context.FeatureContext;

public enum SecurityFeatures implements Feature {

    ALLOW_ORIGIN_FEATURE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}