package de.twenty11.skysail.api.features;

import de.twenty11.skysail.api.features.repository.StateRepository;

public interface FeaturesConfig {

    StateRepository getStateRepository();

    // UserProvider getUserProvider();
}
