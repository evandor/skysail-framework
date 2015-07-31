package io.skysail.server.app.todos.charts;

import io.skysail.server.app.todos.todos.Todo;
import lombok.*;


@Getter
@Setter
@ToString
public class TodoChart {

    private String id;
    private Integer importance;
    private Integer urgency;
    private String list;

    public TodoChart(Todo t) {
        this.id = t.getId();
        this.importance = t.getImportance();
        this.urgency = t.getUrgency();
        this.list = t.getParent();
    }

}
