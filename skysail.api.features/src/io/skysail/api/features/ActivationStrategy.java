package io.skysail.api.features;

/**
 * Implementations define a concrete strategy under which circumstances the
 * feature will be active.
 *
 */
public interface ActivationStrategy {

    String getId();

    String getName();

    boolean isActive(FeatureState state, String user);// SkysailUser user);
}