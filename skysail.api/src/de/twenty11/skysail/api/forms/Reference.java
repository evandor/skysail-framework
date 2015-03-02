package de.twenty11.skysail.api.forms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotate entities' fields with this annotation to indicate that the field
 * references another entity.
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {

    /**
     * the references entity's class.
     */
    Class<?> cls();

    /**
     * a selection provider for choices.
     */
    Class<? extends SelectionProvider> selectionProvider() default IgnoreSelectionProvider.class;

}
