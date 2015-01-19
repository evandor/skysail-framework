package de.twenty11.skysail.server.core.restlet;

import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.api.features.FeaturesConfig;
import de.twenty11.skysail.api.features.repository.StateRepository;

public class SecurityFeaturesConfig implements FeaturesConfig {

    private StateRepository stateRepository;

    @Override
    public StateRepository getStateRepository() {
        return stateRepository;
    }

    @Reference(dynamic = true, multiple = false, optional = true, target = "(name=SecurityFeatures)")
    public void setStateRepository(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public void unsetStateRepository(StateRepository stateRepository) {
        this.stateRepository = null;
    }

}
