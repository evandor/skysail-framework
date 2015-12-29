package io.skysail.server.features;

import io.skysail.api.features.*;
import io.skysail.api.features.annotations.Label;

public enum GuiFeatures implements Feature {

    @Label("show breadcrumbs")
    SHOW_BREADCRUMBS;
    
    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
