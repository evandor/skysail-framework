package io.skysail.server.app.wiki.spaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniquePerOwnerValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniquePerOwner {
    String message() default "This name already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
