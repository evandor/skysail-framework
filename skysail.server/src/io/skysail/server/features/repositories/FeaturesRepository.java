package io.skysail.server.features.repositories;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.features.repository.StateRepository;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class FeaturesRepository implements StateRepository {

    private Map<String, String> config;

    @Activate
    public void activate(Map<String, String> config) {
        this.config = config;
        log.info("activating {} with config {}", this.getClass().getSimpleName(), config);
    }

    @Deactivate
    public void deactivate() {
        config = new HashMap<String, String>();
    }

    @Override
    public FeatureState getFeatureState(Feature feature) {
        boolean enabled = false;
        String featureConfig = config.get(feature.name());
        if (featureConfig != null && new Boolean(featureConfig)) {
            enabled = true;
        }
        return new FeatureState(feature, config, enabled);
    }

    @Override
    public void setFeatureState(FeatureState featureState) {
        throw new UnsupportedOperationException();
    }

}