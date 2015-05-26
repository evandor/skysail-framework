package io.skysail.server.app.todos.todos;

import java.lang.annotation.*;

import javax.validation.*;

@Constraint(validatedBy = ValidStartDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StartDateBeforeDueDate {
    String message() default "The start date must be before the due date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
