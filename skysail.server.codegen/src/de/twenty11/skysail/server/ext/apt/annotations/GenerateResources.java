package de.twenty11.skysail.server.ext.apt.annotations;

import java.lang.annotation.*;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenerateResources {

    //Class<? extends io.skysail.server.app.SkysailApplication> application();
    String application() default "TheApplicationExtendingSkysailApplication";

    ResourceType[] exclude() default {};
}
