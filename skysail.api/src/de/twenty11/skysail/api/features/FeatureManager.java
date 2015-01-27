package de.twenty11.skysail.api.features;

import java.util.List;
import java.util.Set;

import de.twenty11.skysail.api.features.repository.StateRepository;

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
 * <p>
 * see NoFeatureManager - an implementation which will always deny features to
 * be active. This is the default (and fallback-) service with service rank =
 * Integer.MIN_VALUE.
 * </p>
 * 
 * <p>
 * Ideas taken from togglz.org
 * </p>
 */
public interface FeatureManager {

    /**
     * A unique name for this feature manager.
     *
     * @return the name
     */
    String getName();

    /**
     * Provides access to all features the manager is responsible for.
     *
     * @return Set of features, never <code>null</code>
     */
    Set<Feature> getFeatures();

    /**
     * Checks whether the supplied feature is active or not.
     *
     * @param feature
     *            The feature to check
     * @return <code>true</code> if the feature is active, <code>false</code>
     *         otherwise
     */
    boolean isActive(Feature feature);

    FeatureUser getCurrentFeatureUser();

    List<StateRepository> getStateRepositories();

    /**
     * Returns the {@link FeatureState} for the specified feature. This state
     * represents the current configuration of the feature and is typically
     * persisted by a {@link StateRepository} across JVM restarts. The state
     * includes whether the feature is enabled or disabled and the use list.
     *
     * @param feature
     *            The feature to get the state for
     * @return The current state of the feature, never <code>null</code>.
     */
    FeatureState getFeatureState(Feature feature);

    /**
     * Updates the state of a feature. THis allows to enable or disable a
     * feature and to modify the user list associated with the feature.
     *
     * @param state
     *            The new feature state.
     */
    void setFeatureState(FeatureState state);

}
