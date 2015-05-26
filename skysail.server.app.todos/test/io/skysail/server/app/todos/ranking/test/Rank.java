package io.skysail.server.app.todos.ranking.test;

import lombok.Data;

@Data
public class Rank {

    private int importance;
    private int urgency;
    private long rank;

    public Rank(int importance, int urgency, long rank) {
        this.importance = importance;
        this.urgency = urgency;
        this.rank = rank;
        validate();
    }

    private void validate() {
        rangeCheck(importance);
        rangeCheck(urgency);
    }

    private void rangeCheck(int i) {
        if (i < 0 || i > 100) {
            throw new IllegalArgumentException("value must be in between 0 and 100, inclusive");
        }
    }
    
}
