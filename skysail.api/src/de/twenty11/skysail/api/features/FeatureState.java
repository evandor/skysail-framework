package de.twenty11.skysail.api.features;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * The State of a (persisted) feature.
 *
 */
@Getter
public class FeatureState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Feature feature;
    private boolean enabled;
    private String strategyId = "username";
    private final Map<String, String> parameters = new HashMap<String, String>();

    public FeatureState(Feature feature) {
        this(feature, false);
    }

    public FeatureState(Feature feature, boolean enabled) {
        this.feature = feature;
        this.enabled = enabled;
    }

    public FeatureState setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Set<String> getParameterNames() {
        return this.parameters.keySet();
    }

    public Map<String, String> getParameterMap() {
        return Collections.unmodifiableMap(this.parameters);
    }

}
