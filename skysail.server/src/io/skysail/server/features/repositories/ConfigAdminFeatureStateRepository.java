package io.skysail.server.features.repositories;

import io.skysail.api.features.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

/**
 * an implementation using the (file based) configuration provided by the
 * OSGi configAdmin.
 * 
 * <p>As usual, the configuration resides in a file with the fully qualified
 * name of this class, postfixed with ".cfg", in the config folder.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class ConfigAdminFeatureStateRepository implements FeatureStateRepository {

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
