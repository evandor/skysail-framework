package io.skysail.api.features;

/**
 * a feature user identified by her name.
 *
 */
public class FeatureUser {

    private String name;

    public FeatureUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}