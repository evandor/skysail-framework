package de.twenty11.skysail.server.um.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PasswordsMatch {

    String message() default "The provided passwords do not match.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}