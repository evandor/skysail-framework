package io.skysail.server.app.todos.todos.resources;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.app.todos.todos.Todo;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

@Getter
public class TimelineTodo implements Identifiable {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Field
    private String id;

    @Field
    private String content;

    private String type;

    @Field
    private String start;

    private Object end;

    public TimelineTodo(Todo todo) {
        if (todo.getDue() != null) {
           this.start = new SimpleDateFormat(DATE_FORMAT).format(todo.getDue());
        } else {
           this.start = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        }
        this.id = todo.getId();
        this.content = todo.getTitle();
        this.end = null;
        this.type = "point";
    }

    @Override
    public void setId(String id) {
    }


}
