package io.skysail.server.app.todos.columns;

import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;

import java.util.*;

import lombok.Getter;

@Getter
public class TodoColumn {

    private String name;
    private List<Todo> todos = new ArrayList<>();

    public TodoColumn(Status status) {
        this.name = status.name();
    }

    public void add(Todo todo) {
        todos .add(todo);
    }

}
