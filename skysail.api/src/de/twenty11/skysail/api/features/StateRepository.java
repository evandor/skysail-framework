package de.twenty11.skysail.api.features;


public interface StateRepository {

	FeatureState getFeatureState(FeatureToggle feature);

    void setFeatureState(FeatureState featureState);

}
