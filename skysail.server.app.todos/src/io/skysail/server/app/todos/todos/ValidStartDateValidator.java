package io.skysail.server.app.todos.todos;

import javax.validation.*;

public class ValidStartDateValidator implements ConstraintValidator<StartDateBeforeDueDate, Todo> {

    @Override
    public void initialize(StartDateBeforeDueDate arg0) {
    }

    @Override
    public boolean isValid(Todo todo, ConstraintValidatorContext ctx) {
        if (todo.getStartDate() == null || todo.getDue() == null) {
            return true;
        }
        if (todo.getDue().equals(todo.getStartDate()) || todo.getDue().after(todo.getStartDate())) {
            return true;
        }
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate("the start date must be before the due date").addPropertyNode("startDate")
                .addConstraintViolation();
        return false;
    }

}
