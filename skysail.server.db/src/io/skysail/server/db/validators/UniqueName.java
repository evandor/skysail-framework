package io.skysail.server.db.validators;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import io.skysail.domain.Nameable;

@Constraint(validatedBy = UniqueNameValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UniqueName {

    String message() default "This name already exists";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};
    
    /**
     * The Class of the entity in question.
     */
    Class<? extends Nameable> entityClass();


}
