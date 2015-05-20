package io.skysail.server.app.todos.todos;

import java.lang.annotation.*;

import javax.validation.*;

@Constraint(validatedBy = ValidListIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidListId {
    String message() default "This list does not exist or has another owner";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
