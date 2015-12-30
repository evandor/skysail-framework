package io.skysail.domain.html;

import java.lang.annotation.*;

/**
 * annotate entities' fields with this annotation to indicate that the field
 * references another entity.
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Relation {

}
