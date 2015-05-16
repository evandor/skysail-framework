package io.skysail.api.features;

/**
 *
 * <p>
 * This interface represents a feature and is typically implemented by a feature
 * enum.
 * </p>
 *
 * <p>
 * Usually it makes sense to implement the following method which allows a very
 * easy way to check the status of a feature.
 * </p>
 *
 * <pre>
 * public boolean isActive() {
 *     return FeatureContext.getFeatureManager().isActive(this);
 * }
 * </pre>
 *
 * <p>
 * This is an adaption of the togglz library (http://www.togglz.org/).
 * </p>
 * 
 *
 */
public interface Feature {

    /**
     * Returns a textual representation of the feature. This method is
     * implicitly implemented as feature typically are enumerations.
     * 
     */
    String name();

}
