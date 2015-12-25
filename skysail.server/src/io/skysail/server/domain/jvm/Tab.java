package io.skysail.server.domain.jvm;

import lombok.*;

@Getter
@Setter
@ToString
public class Tab {

    private final String name;
    private final int counter;

    public Tab(@NonNull String name, int counter) {
        this.name = name;
        this.counter = counter;
    }
    
    public boolean isActive() {
        return counter == 0;
    }
}
