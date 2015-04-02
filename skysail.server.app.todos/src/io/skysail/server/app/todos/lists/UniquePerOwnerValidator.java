package io.skysail.server.app.todos.lists;

import io.skysail.server.app.todos.TodoList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePerOwnerValidator implements ConstraintValidator<UniquePerOwner, TodoList> {

    @Override
    public void initialize(UniquePerOwner arg0) {
    }

    @Override
    public boolean isValid(TodoList todoList, ConstraintValidatorContext context) {
        return false;
    }

}
