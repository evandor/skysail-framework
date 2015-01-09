package de.twenty11.skysail.api.features;

import de.twenty11.skysail.api.um.SkysailUser;

public interface ActivationStrategy {

    String getId();

    String getName();

    boolean isActive(FeatureState state, SkysailUser user);
}