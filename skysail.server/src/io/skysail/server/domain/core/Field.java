package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;
import lombok.*;

/**
 * Part of skysail's core domain: A Field belongs to an entity which belongs to an application.
 *
 */
@Getter
@Setter
@ToString
public class Field implements Identifiable {

    public Field(String id) {
        this.id = id;
    }

    private String id;
}
