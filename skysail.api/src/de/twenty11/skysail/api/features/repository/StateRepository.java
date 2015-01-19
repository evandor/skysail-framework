package de.twenty11.skysail.api.features.repository;

import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.FeatureState;

public interface StateRepository {

    FeatureState getFeatureState(Feature feature);

    void setFeatureState(FeatureState featureState);

}