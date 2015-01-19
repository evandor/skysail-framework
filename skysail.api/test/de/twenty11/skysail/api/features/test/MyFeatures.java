package de.twenty11.skysail.api.features.test;

import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.annotations.EnabledByDefault;
import de.twenty11.skysail.api.features.annotations.Label;
import de.twenty11.skysail.api.features.context.FeatureContext;

public enum MyFeatures implements Feature {

    @Label("First Feature")
    FEATURE_ONE,

    @EnabledByDefault
    @Label("Second Feature")
    FEATURE_TWO;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}
