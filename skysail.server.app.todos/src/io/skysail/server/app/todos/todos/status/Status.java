package io.skysail.server.app.todos.todos.status;

import java.util.*;

import lombok.Getter;

@Getter
public enum Status {

    NEW("NEW","yellow", "PLANNED", "WIP", "ARCHIVED"),
    PLANNED("PLANNED","green", "WIP", "ARCHIVED"),
    WIP("WIP","orange","ARCHIVED"),
    FINISHED("FINISHED", "gray","ARCHIVED"),
    ARCHIVED("ARCHIVED", "gray","ARCHIVED");

    private List<String> nexts = new ArrayList<>();

    private String color;

    Status() {
        nexts = Collections.emptyList();
    }

    Status(String name, String color, String... nextStatuses) {
        this.color = color;
        nexts.add(name);
        for (String str : nextStatuses) {
            nexts.add(str);
        }
    }

    public boolean isActive() {
        return !this.equals(FINISHED) && !this.equals(ARCHIVED);
    }
}
