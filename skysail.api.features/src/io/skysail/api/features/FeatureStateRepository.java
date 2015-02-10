package io.skysail.api.features;

import aQute.bnd.annotation.ProviderType;

/**
 * Implementors retrieve or update the current featureState for a feature based
 * on some persistence scheme.
 *
 */
@ProviderType
public interface FeatureStateRepository {

    FeatureState getFeatureState(Feature feature);

    void setFeatureState(FeatureState featureState);

}