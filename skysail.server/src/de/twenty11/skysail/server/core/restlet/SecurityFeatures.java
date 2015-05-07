package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.features.*;
import io.skysail.api.features.annotations.Label;

public enum SecurityFeatures implements Feature {

    @Label("to be documented")
    ALLOW_ORIGIN_FEATURE,
    
    @Label("toggle usage of the cache for user credentials")
    USE_CREDENTIALS_CACHE_FEATURE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}