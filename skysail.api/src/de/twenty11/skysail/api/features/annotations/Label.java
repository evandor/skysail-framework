package de.twenty11.skysail.api.features.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Label {
    /**
     * The human readable label of this feature
     *
     * @return The label
     */
    String value();
}