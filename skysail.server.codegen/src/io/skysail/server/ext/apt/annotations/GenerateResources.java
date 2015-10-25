package io.skysail.server.ext.apt.annotations;

import java.lang.annotation.*;

import de.twenty11.skysail.server.ext.apt.annotations.ResourceType;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenerateResources {

    //Class<? extends io.skysail.server.app.SkysailApplication> application();
    String application() default "TheApplicationExtendingSkysailApplication";

    ResourceType[] exclude() default {};
}