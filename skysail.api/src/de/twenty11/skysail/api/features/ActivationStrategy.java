package de.twenty11.skysail.api.features;


public interface ActivationStrategy {

    String getId();

    String getName();

    boolean isActive(FeatureState state, String user);// SkysailUser user);
}