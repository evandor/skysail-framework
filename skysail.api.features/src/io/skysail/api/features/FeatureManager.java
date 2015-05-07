package io.skysail.api.features;

import java.util.*;

/**
 * A FeatureManager is responsible for the decision whether a certain feature is
 * active or not.
 *
 * <p>
 * Multiple bundles can implement FeatureManager implementations and offer them
 * as OSGi services; the FeatureContext is the class to ask for the _one_ active
 * FeatuerManager at a certain time (which will be the service with the highest
 * service rank).
 * </p>
 * 
 * <p>
 * Ideas taken from togglz.org
 * </p>
 */
public interface FeatureManager {

    String getName();

    Set<Feature> getFeatures();

    boolean isActive(Feature feature);

    FeatureUser getCurrentFeatureUser();

    List<FeatureStateRepository> getStateRepositories();

    FeatureState getFeatureState(Feature feature);

    void setFeatureState(FeatureState state);

}
