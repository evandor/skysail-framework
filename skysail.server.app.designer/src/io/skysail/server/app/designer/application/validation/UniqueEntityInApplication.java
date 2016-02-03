package io.skysail.server.app.designer.application.validation;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueEntityInApplicationValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueEntityInApplication {

    String message() default "This name already exists";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};
}
