package de.twenty11.skysail.api.features.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.twenty11.skysail.api.features.repository.StateRepository;

/**
 * Allows to specify that the annotated feature should be enabled by default if
 * the {@link StateRepository} doesn't have any state saved.
 *
 * <p>
 * original @author Christian Kaltepoth
 * </p>
 * 
 * @see togglz.org
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnabledByDefault {
}