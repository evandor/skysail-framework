package io.skysail.server.app.todos.todos.status;

import java.util.*;

import lombok.Getter;

@Getter
public enum Status {

    
    NEW("NEW", "PLANNED", "WIP", "POSTPONED"), 
    PLANNED("PLANNED", "WIP","ARCHIVED"), 
    WIP("WIP", "POSTPONED","ARCHIVED"), 
    POSTPONED("POSTPONED", "PLANNED", "WIP", "ARCHIVED"), 
    FINISHED("ARCHIVED", "REOPENED"),
    REOPENED("PLANNED", "WIP", "POSTPONED"),
    ARCHIVED("ARCHIVED");
    
    private List<String> nexts = new ArrayList<>();
   
    Status() {
        nexts = Collections.emptyList();
    }
    
    Status(String first, String... nextStatuses) {
        nexts.add(first);
        for (String str : nextStatuses) {
            nexts.add(str);
        }
    }
    
    public boolean isActive() {
        return !this.equals(FINISHED) && !this.equals(ARCHIVED);
    }
}
