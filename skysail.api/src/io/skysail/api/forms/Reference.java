package io.skysail.api.forms;

import java.lang.annotation.*;

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
     * a selection provider for choices.
     */
    Class<? extends SelectionProvider> selectionProvider() default IgnoreSelectionProvider.class;

}
