package io.skysail.server.app.todos.todos.status;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.Getter;

@Getter
public enum Status implements Identifiable{

    NEW("NEW","yellow", "WIP", "ARCHIVED"),
    @Deprecated // planned will be: due date != null && now < due date
    PLANNED("PLANNED","green", "WIP", "DONE"),
    WIP("WIP","orange", "DONE"),
    @Deprecated // renamed to done
    FINISHED("FINISHED", "gray", "DONE"),
    DONE("DONE", "gray"),
    @Deprecated // archived will be: done & done date + 7 < now
    ARCHIVED("ARCHIVED", "gray","DONE");

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
        return !this.equals(FINISHED) && !this.equals(ARCHIVED) && !this.equals(DONE);
    }

    @Override
    public String getId() {
        return name();
    }

    @Override
    public void setId(String id) {
    }
}
