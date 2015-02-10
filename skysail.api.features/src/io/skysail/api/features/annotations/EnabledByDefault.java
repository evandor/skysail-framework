package io.skysail.api.features.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to specify that the annotated feature should be enabled by default if
 * the {@link FeatureStateRepository} doesn't have any state saved.
 *
 * <p>
 * original @author Christian Kaltepoth
 * </p>
 *
 * <p>
 * see togglz.org
 * </p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnabledByDefault {
}