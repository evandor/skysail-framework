package io.skysail.server.forms;

import lombok.*;

@Data
@EqualsAndHashCode(of = "identifier")
public class Tab {

    private final String identifier;
    private final String label;
    private final int counter;


    public boolean isActive() {
        return counter == 1;
    }
}
