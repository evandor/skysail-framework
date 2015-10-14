package de.twenty11.skysail.server.ext.apt.annotations;

import java.lang.annotation.*;

/**
 * Marker interface; removing this from a generated class will let APT ignore that class and
 * therefore leave your implementation alone.
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Generated {

}
