package de.twenty11.skysail.api.features;

/**
 * The central concept: a FeatureToggle is identified by a name and provides a way to get queried to determine if it is
 * active or not.
 * 
 * Typically, the FeatureToggle implementation will delegate this decision (active or not) to a {@link FeatureManager}.
 * 
 */
public interface FeatureToggle {

    String name();

    boolean isActive();

}
