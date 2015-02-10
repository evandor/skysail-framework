package io.skysail.api.features.test;

import io.skysail.api.features.Feature;
import io.skysail.api.features.FeatureContext;
import io.skysail.api.features.annotations.EnabledByDefault;
import io.skysail.api.features.annotations.Label;

public enum MyFeatures implements Feature {

    @Label("First Feature")
    FEATURE_ONE,

    @EnabledByDefault
    @Label("Second Feature")
    FEATURE_TWO,

    FEATURE_THREE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}
