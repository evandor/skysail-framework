package io.skysail.server.app.todos.todos.status;

public enum Status {

    NEW("PLANNED","WIP","POSTPHONED"), PLANNED("WIP"), WIP("POSTPONED"), POSTPONED("CLOSED"), CLOSED(), FINISHED();
    
    private String[] next;
    
    Status(String... nextStatus) {
        this.next = nextStatus;
    }
}