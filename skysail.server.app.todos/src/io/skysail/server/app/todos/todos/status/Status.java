package io.skysail.server.app.todos.todos.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public enum Status {

    
    NEW("NEW", "PLANNED","WIP","POSTPONED"), 
    PLANNED("PLANNED", "WIP","CLOSED"), 
    WIP("WIP", "POSTPONED"), 
    POSTPONED("POSTPONED", "PLANNED", "WIP", "CLOSED"), 
    CLOSED("CLOSED", "FINISHED"), 
    FINISHED();
    
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
}
