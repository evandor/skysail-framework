package io.skysail.api.features;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * The State of a (persisted) feature.
 *
 * <p>
 * A feature is active if it is enabled and there is either no activation
 * strategy or the strategy evaluates to true.
 * </p>
 * <p>
 * A feature is inactive if it is either disabled or there is a strategy defined
 * which evaluates to false.
 * </p>
 */
@Getter
public class FeatureState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Feature feature;
    private boolean enabled;
    private String strategyId = "username";
    private final Map<String, String> parameters = new HashMap<String, String>();
    private Map<String, String> config;

    public FeatureState(Feature feature) {
        this(feature, Collections.emptyMap(), false);
    }

    /**
     * @param feature
     * @param config
     * @param enabled
     */
    public FeatureState(Feature feature, Map<String, String> config, boolean enabled) {
        this.feature = feature;
        this.config = config;
        this.enabled = enabled;
    }

    public FeatureState setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Set<String> getParameterNames() {
        return this.config.keySet();
    }

    public Map<String, String> getConfig() {
        return Collections.unmodifiableMap(this.config);
    }

}
