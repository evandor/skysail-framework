package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import lombok.*;

@Getter
@Setter
@ToString
public class Field implements Identifiable {

    public Field(String id) {
        this.id = id;
    }

    private String id;
}
