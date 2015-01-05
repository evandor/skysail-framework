package de.twenty11.skysail.api.features;

import java.util.List;
import java.util.Set;

/**
 * A FeatureManager is responsible for the decision whether a certain feature is
 * active or not.
 *
 * Multiple bundles can implement FeatureManager implementations and offer them
 * as OSGi services; the {@link FeatureContext} is the class to ask for the
 * _one_ active FeatuerManager at a certain time (which will be the service with
 * the highest service rank).
 *
 * @see NoFeatureManager - an implementation which will always deny features to
 *      be active. This is the default (and fallback-) service with service rank = Integer.MIN_VALUE.
 *
 */
public interface FeatureManager {

	/**
	 * This feature manager's name.
	 */
	String getName();

	Set<FeatureToggle> getFeatures();

	boolean isActive(OsgiFeatureToggle features);

	FeatureUser getCurrentFeatureUser();

	List<StateRepository> getStateRepositories();

}
