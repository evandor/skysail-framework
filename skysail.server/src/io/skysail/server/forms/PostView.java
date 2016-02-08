package io.skysail.server.forms;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostView {

    Visibility visibility() default Visibility.SHOW;

    /**
     * A string identifier used to organize fields in tabs.
     */
    String tab() default "";

}
